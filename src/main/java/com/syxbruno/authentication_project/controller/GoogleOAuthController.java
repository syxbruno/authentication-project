package com.syxbruno.authentication_project.controller;

import com.syxbruno.authentication_project.dto.response.auth.AuthTokenResponse;
import com.syxbruno.authentication_project.model.User;
import com.syxbruno.authentication_project.repository.UserRepository;
import com.syxbruno.authentication_project.service.GoogleOAuthService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/google")
public class GoogleOAuthController {

  private final GoogleOAuthService service;
  private final UserRepository repository;

  @GetMapping
  public ResponseEntity<Void> redirectGoogle() {

    String url = service.generateUrl();

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(url));

    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }

  @GetMapping("/auth")
  public ResponseEntity<AuthTokenResponse> authUserOAuth(@RequestParam String code) {

    String email = service.getEmail(code);
    User user = repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

    AuthTokenResponse response = service.authUserOauth(user);

    return ResponseEntity.ok(response);
  }
}
