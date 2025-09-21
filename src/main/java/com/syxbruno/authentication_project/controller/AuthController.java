package com.syxbruno.authentication_project.controller;

import com.syxbruno.authentication_project.dto.request.auth.AuthCodeRequest;
import com.syxbruno.authentication_project.dto.request.auth.AuthDataA2f;
import com.syxbruno.authentication_project.dto.request.auth.AuthLoginRequest;
import com.syxbruno.authentication_project.dto.request.auth.AuthRefreshTokenRequest;
import com.syxbruno.authentication_project.dto.request.auth.AuthRegisterRequest;
import com.syxbruno.authentication_project.dto.response.auth.AuthTokenResponse;
import com.syxbruno.authentication_project.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody @Valid AuthRegisterRequest data) {

    String response = authService.register(data);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/verify")
  public ResponseEntity<String> verifyEmail(@RequestBody @Valid AuthCodeRequest data) {

    authService.verifyEmail(data);
    return ResponseEntity.ok("Email verified successfully");
  }

  @PostMapping("/login")
  public ResponseEntity<AuthTokenResponse> login(@RequestBody @Valid AuthLoginRequest data) {

    AuthTokenResponse response = authService.login(data);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/update-token")
  public ResponseEntity<AuthTokenResponse> updateToken(@RequestBody @Valid AuthRefreshTokenRequest data) {

    AuthTokenResponse response = authService.updateToken(data);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/verify-a2f")
  public ResponseEntity<AuthTokenResponse> verifyA2f(@RequestBody @Valid AuthDataA2f data) {

    AuthTokenResponse response = authService.verifyA2f(data);
    return ResponseEntity.ok(response);
  }
}