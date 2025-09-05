package com.syxbruno.authentication_project.service;

import com.syxbruno.authentication_project.dto.user.request.UserLoginRequest;
import com.syxbruno.authentication_project.dto.user.request.UserRegisterRequest;
import com.syxbruno.authentication_project.dto.user.response.UserLoginResponse;
import com.syxbruno.authentication_project.exception.UserResponseException;
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
    return repository.findByEmail(username).orElseThrow(() -> new UserResponseException("This email is not registered in the database"));
  }

  public UserLoginResponse login(UserLoginRequest data) {

    UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
    Authentication auth = manager.authenticate(usernamePassword);

      String token = service.generateToken((User) auth.getPrincipal());
      return new UserLoginResponse(token);
  }


  public void register(@Valid UserRegisterRequest data) {

    if (repository.findByEmail(data.email()) != null) {

      throw new UserResponseException("This email is already in use");
    }

    String encryptedPassword = encoder.encode(data.password());
    User user = new User(data.name(), data.email(), encryptedPassword);

    repository.save(user);
  }
}
