package com.syxbruno.authentication_project.controller.user;

import com.syxbruno.authentication_project.dto.user.request.UserLoginRequest;
import com.syxbruno.authentication_project.dto.user.request.UserRegisterRequest;
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
  public ResponseEntity login(@RequestBody @Valid UserLoginRequest data) {

    service.login(data);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/register")
  public ResponseEntity register(@RequestBody @Valid UserRegisterRequest data) {

    service.register(data);
    return ResponseEntity.ok().build();
  }
}
