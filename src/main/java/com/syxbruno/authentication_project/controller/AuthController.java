package com.syxbruno.authentication_project.controller;

import com.syxbruno.authentication_project.dto.request.auth.AuthLoginRequest;
import com.syxbruno.authentication_project.dto.request.auth.AuthRefreshTokenRequest;
import com.syxbruno.authentication_project.dto.request.auth.AuthRegisterRequest;
import com.syxbruno.authentication_project.dto.request.auth.AuthVerifyRequest;
import com.syxbruno.authentication_project.dto.response.auth.AuthTokenResponse;
import com.syxbruno.authentication_project.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService service;

  @PatchMapping("/register")
  public ResponseEntity<String> register(@RequestBody @Valid AuthRegisterRequest data) {

    String response = service.register(data);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/verify")
  public ResponseEntity<String> verifyEmail(@RequestBody @Valid AuthVerifyRequest data) {

    service.verifyEmail(data);
    return ResponseEntity.ok("Email verified successfully");
  }

  @PatchMapping("/login")
  public ResponseEntity<AuthTokenResponse> login(@RequestBody @Valid AuthLoginRequest data) {

    AuthTokenResponse response = service.login(data);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/update-token")
  public ResponseEntity<AuthTokenResponse> updateToken(@RequestBody @Valid AuthRefreshTokenRequest data) {

    AuthTokenResponse response = service.updateToken(data);
    return ResponseEntity.ok(response);
  }
}