package com.syxbruno.authentication_project.controller;

import com.syxbruno.authentication_project.dto.request.auth.AuthLoginRequest;
import com.syxbruno.authentication_project.dto.request.auth.AuthRefreshTokenRequest;
import com.syxbruno.authentication_project.dto.request.auth.AuthRegisterRequest;
import com.syxbruno.authentication_project.dto.response.auth.AuthTokenResponse;
import com.syxbruno.authentication_project.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService service;

  @PostMapping("/login")
  public ResponseEntity<AuthTokenResponse> login(@RequestBody @Valid AuthLoginRequest data) {

    AuthTokenResponse response = service.login(data);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody @Valid AuthRegisterRequest data) {

    service.register(data);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/update-token")
  public ResponseEntity<AuthTokenResponse> updateToken(@RequestBody @Valid AuthRefreshTokenRequest data) {

    AuthTokenResponse response = service.updateToken(data);
    return ResponseEntity.ok(response);
  }
}