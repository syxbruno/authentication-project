package com.syxbruno.authentication_project.service;

import com.syxbruno.authentication_project.dto.request.auth.AuthLoginRequest;
import com.syxbruno.authentication_project.dto.request.auth.AuthRefreshTokenRequest;
import com.syxbruno.authentication_project.dto.request.auth.AuthRegisterRequest;
import com.syxbruno.authentication_project.dto.request.auth.AuthVerifyRequest;
import com.syxbruno.authentication_project.dto.response.auth.AuthTokenResponse;
import com.syxbruno.authentication_project.exception.BusinessRules;
import com.syxbruno.authentication_project.model.Profiles;
import com.syxbruno.authentication_project.model.User;
import com.syxbruno.authentication_project.model.enums.ProfilesName;
import com.syxbruno.authentication_project.repository.ProfilesRepository;
import com.syxbruno.authentication_project.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final TokenService service;
  private final PasswordEncoder encoder;
  private final EmailService emailService;
  private final AuthenticationManager manager;
  private final UserRepository userRepository;
  private final ProfilesRepository profilesRepository;

  public String register(AuthRegisterRequest data) {

    if (userRepository.findByEmail(data.email()).isPresent()) {

      throw new BusinessRules("This email is already in use");
    }

    String encryptedPassword = encoder.encode(data.password());
    Profiles profileUser = profilesRepository.findByName(ProfilesName.STUDENT)
        .orElseThrow(() -> new UsernameNotFoundException("Profile not found"));

    User user = new User(data.name(), data.email(), encryptedPassword, profileUser, false);
    user.setToken(UUID.randomUUID().toString());
    user.setTokenExpiration(LocalDateTime.now().plusMinutes(30));

    userRepository.save(user);
    emailService.sendEmailVerifyUser(user);

    return "A code was sent by email, before any request the user must check the email";
  }

  public void verifyEmail(AuthVerifyRequest data) {

    User user = userRepository.findByToken(data.code()).orElseThrow(() -> new BusinessRules("Invalid token"));

    if (user.getTokenExpiration().isBefore(LocalDateTime.now())) {

      throw new BusinessRules("The token has expired");
    }

    user.setVerifiedEmail(true);
    user.setToken(null);
    user.setTokenExpiration(null);

    userRepository.save(user);
  }

  public AuthTokenResponse login(AuthLoginRequest data) {

    UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
    Authentication auth = manager.authenticate(usernamePassword);

    String token = service.generateToken((User) auth.getPrincipal());
    String refreshToken = service.generateRefreshToken((User) auth.getPrincipal());

    return new AuthTokenResponse(token, refreshToken);
  }

  public AuthTokenResponse updateToken(AuthRefreshTokenRequest data) {

    String refreshToken = data.refreshToken();
    Long id = Long.valueOf(service.validateToken(refreshToken));

    User user = userRepository.findById(id).orElseThrow(() -> new BusinessRules("User not found"));

    String token = service.generateToken(user);
    String newRefreshToken = service.generateRefreshToken(user);

    return new AuthTokenResponse(token, newRefreshToken);
  }
}
