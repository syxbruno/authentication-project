package com.syxbruno.authentication_project.service;

import com.syxbruno.authentication_project.dto.request.auth.AuthCodeRequest;
import com.syxbruno.authentication_project.dto.request.auth.AuthDataA2f;
import com.syxbruno.authentication_project.dto.request.auth.AuthLoginRequest;
import com.syxbruno.authentication_project.dto.request.auth.AuthRefreshTokenRequest;
import com.syxbruno.authentication_project.dto.request.auth.AuthRegisterRequest;
import com.syxbruno.authentication_project.dto.response.auth.AuthTokenResponse;
import com.syxbruno.authentication_project.exception.BusinessRules;
import com.syxbruno.authentication_project.model.Profiles;
import com.syxbruno.authentication_project.model.User;
import com.syxbruno.authentication_project.model.enums.ProfilesName;
import com.syxbruno.authentication_project.repository.ProfilesRepository;
import com.syxbruno.authentication_project.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
  private final TotpService totpService;
  private final EmailService emailService;
  private final AuthenticationManager manager;
  private final UserRepository userRepository;
  private final ProfilesRepository profilesRepository;

  @Transactional
  public String register(AuthRegisterRequest data) {

    if (userRepository.findByEmail(data.email()).isPresent()) {

      throw new BusinessRules("This email is already in use");
    }

    String encryptedPassword = encoder.encode(data.password());
    Profiles profileUser = profilesRepository.findByName(ProfilesName.STUDENT)
        .orElseThrow(() -> new UsernameNotFoundException("Profile not found"));

    User user = new User(data.name(), data.email(), encryptedPassword, profileUser, false, false);
    user.setToken(UUID.randomUUID().toString());
    user.setTokenExpiration(LocalDateTime.now().plusMinutes(30));

    userRepository.save(user);
    emailService.sendEmailVerifyUser(user);

    return "A code was sent by email, before any request the user must check the email";
  }

  @Transactional
  public void verifyEmail(AuthCodeRequest data) {

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

    User user = (User) auth.getPrincipal();

    if (user.getA2f()) {

      return new AuthTokenResponse(null, null, true);
    }

    String token = service.generateToken(user);
    String refreshToken = service.generateRefreshToken(user);

    return new AuthTokenResponse(token, refreshToken, false);
  }

  public AuthTokenResponse updateToken(AuthRefreshTokenRequest data) {

    String refreshToken = data.refreshToken();
    Long id = Long.valueOf(service.validateToken(refreshToken));

    User user = userRepository.findById(id).orElseThrow(() -> new BusinessRules("User not found"));

    String token = service.generateToken(user);
    String newRefreshToken = service.generateRefreshToken(user);

    return new AuthTokenResponse(token, newRefreshToken, false);
  }

  public AuthTokenResponse verifyA2f(AuthDataA2f data) {

    User user = userRepository.findByEmail(data.email())
        .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

    if (!user.getA2f()) {

      throw new BusinessRules("To use this feature the user must activate A2F");
    }

    Boolean validCode = totpService.verifyCode(data.code(), user);

    if (!validCode) {

      throw new BadCredentialsException("Invalid code");
    }

    String token = service.generateToken(user);
    String newRefreshToken = service.generateRefreshToken(user);

    return new AuthTokenResponse(token, newRefreshToken, true);
  }
}
