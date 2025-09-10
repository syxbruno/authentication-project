package com.syxbruno.authentication_project.service;

import com.syxbruno.authentication_project.dto.user.request.UserLoginRequest;
import com.syxbruno.authentication_project.dto.user.request.UserRefreshTokenRequest;
import com.syxbruno.authentication_project.dto.user.request.UserRegisterRequest;
import com.syxbruno.authentication_project.dto.user.response.UserTokenResponse;
import com.syxbruno.authentication_project.exception.BusinessRules;
import com.syxbruno.authentication_project.model.User;
import com.syxbruno.authentication_project.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final TokenService service;
  private final PasswordEncoder encoder;
  private final UserRepository repository;
  private final AuthenticationManager manager;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository.findByEmail(username).orElseThrow(() -> new BusinessRules("This email is not registered in the database"));
  }

  public UserTokenResponse login(UserLoginRequest data) {

    UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
    Authentication auth = manager.authenticate(usernamePassword);

    String token = service.generateToken((User) auth.getPrincipal());
    String refreshToken = service.generateRefreshToken((User) auth.getPrincipal());

    return new UserTokenResponse(token, refreshToken);
  }


  public void register(@Valid UserRegisterRequest data) {

    if (repository.findByEmail(data.email()).isPresent()) {

      throw new BusinessRules("This email is already in use");
    }

    String encryptedPassword = encoder.encode(data.password());
    User user = new User(data.name(), data.email(), encryptedPassword);

    repository.save(user);
  }

  public UserTokenResponse updateToken(@Valid UserRefreshTokenRequest data) {

    String refreshToken = data.refreshToken();
    Long id = Long.valueOf(service.validateToken(refreshToken));

    User user = repository.findById(id).orElseThrow(() -> new BusinessRules("User not found"));

    String token = service.generateToken(user);
    String newRefreshToken = service.generateRefreshToken(user);

    return new UserTokenResponse(token, newRefreshToken);
  }
}
