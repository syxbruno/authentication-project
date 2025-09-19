package com.syxbruno.authentication_project.service;

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
import jakarta.validation.Valid;
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
  private final UserRepository userRepository;
  private final ProfilesRepository profilesRepository;
  private final AuthenticationManager manager;

  public void register(@Valid AuthRegisterRequest data) {

    if (userRepository.findByEmail(data.email()).isPresent()) {

      throw new BusinessRules("This email is already in use");
    }

    String encryptedPassword = encoder.encode(data.password());
    Profiles profileUser = profilesRepository.findByName(ProfilesName.STUDENT).orElseThrow(() -> new UsernameNotFoundException("Profile not found"));

    User user = new User(data.name(), data.email(), encryptedPassword, profileUser);

    userRepository.save(user);
  }

  public AuthTokenResponse login(AuthLoginRequest data) {

    UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
    Authentication auth = manager.authenticate(usernamePassword);

    String token = service.generateToken((User) auth.getPrincipal());
    String refreshToken = service.generateRefreshToken((User) auth.getPrincipal());

    return new AuthTokenResponse(token, refreshToken);
  }

  public AuthTokenResponse updateToken(@Valid AuthRefreshTokenRequest data) {

    String refreshToken = data.refreshToken();
    Long id = Long.valueOf(service.validateToken(refreshToken));

    User user = userRepository.findById(id).orElseThrow(() -> new BusinessRules("User not found"));

    String token = service.generateToken(user);
    String newRefreshToken = service.generateRefreshToken(user);

    return new AuthTokenResponse(token, newRefreshToken);
  }
}
