package com.syxbruno.authentication_project.controller;

import com.syxbruno.authentication_project.dto.user.request.UserLoginRequest;
import com.syxbruno.authentication_project.dto.user.request.UserRefreshTokenRequest;
import com.syxbruno.authentication_project.dto.user.request.UserRegisterRequest;
import com.syxbruno.authentication_project.dto.user.response.UserTokenResponse;
import com.syxbruno.authentication_project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService service;

  @PostMapping("/login")
  public ResponseEntity<UserTokenResponse> login(@RequestBody @Valid UserLoginRequest data) {

    UserTokenResponse response = service.login(data);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody @Valid UserRegisterRequest data) {

    service.register(data);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/update-token")
  public ResponseEntity<UserTokenResponse> updateToken(@RequestBody @Valid UserRefreshTokenRequest data) {

    UserTokenResponse response = service.updateToken(data);
    return ResponseEntity.ok(response);
  }
}