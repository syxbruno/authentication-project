package com.syxbruno.authentication_project.controller;

import com.syxbruno.authentication_project.dto.request.user.UserAlterPasswordRequest;
import com.syxbruno.authentication_project.dto.request.user.UserProfileRequest;
import com.syxbruno.authentication_project.dto.response.user.UserResponse;
import com.syxbruno.authentication_project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService service;

  @PatchMapping("/reset-password")
  public ResponseEntity<String> sendTokenEmail(@RequestBody String email) {

    service.sendToken(email);
    return ResponseEntity.ok("An email has been sent to you, follow the steps to change your password");
  }

  @PostMapping("/alter-password")
  public ResponseEntity<String> alterPassword(@RequestBody UserAlterPasswordRequest data) {

    service.alterPassword(data);
    return ResponseEntity.ok("Your password has been changed successfully");
  }

  @PatchMapping("/add-profile/{id}")
  public ResponseEntity<UserResponse> addProfile(@PathVariable Long id, @RequestBody @Valid UserProfileRequest data) {

    UserResponse response = service.addProfile(id, data);
    return ResponseEntity.ok(response);
  }
}