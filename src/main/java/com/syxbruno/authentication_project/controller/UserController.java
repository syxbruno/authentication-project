package com.syxbruno.authentication_project.controller;

import com.syxbruno.authentication_project.dto.request.user.UserAlterPasswordRequest;
import com.syxbruno.authentication_project.dto.request.user.UserProfileRequest;
import com.syxbruno.authentication_project.dto.request.user.UserSendTokenRequest;
import com.syxbruno.authentication_project.dto.response.user.UserResponse;
import com.syxbruno.authentication_project.model.User;
import com.syxbruno.authentication_project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/send-code")
  public ResponseEntity<String> sendTokenEmail(@Valid @RequestBody UserSendTokenRequest data) {

    String send = userService.sendToken(data.email());
    return ResponseEntity.ok(send);
  }

  @PatchMapping("/alter-password")
  public ResponseEntity<String> alterPassword(@Valid @RequestBody UserAlterPasswordRequest data) {

    userService.alterPassword(data);
    return ResponseEntity.ok("Your password has been changed successfully");
  }

  @PatchMapping("/add-profile/{id}")
  public ResponseEntity<UserResponse> addProfile(@PathVariable Long id, @Valid @RequestBody UserProfileRequest data) {

    UserResponse response = userService.addProfile(id, data);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/a2f")
  public ResponseEntity<String> generateQrCode(@AuthenticationPrincipal User user) {

    String url = userService.generateQrCode(user);
    return ResponseEntity.ok(url);
  }

  @PatchMapping("/disable-a2f")
  public ResponseEntity<Void> disableA2f(@AuthenticationPrincipal User user) {

    userService.disableA2f(user);
    return ResponseEntity.noContent().build();
  }
}