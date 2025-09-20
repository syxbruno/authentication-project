package com.syxbruno.authentication_project.controller;

import com.syxbruno.authentication_project.dto.response.auth.AuthTokenResponse;
import com.syxbruno.authentication_project.service.LoginGoogleService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login/google")
public class LoginGoogleController {

  private final LoginGoogleService loginGoogleService;

  @GetMapping
  public ResponseEntity<Void> redirectGoogle(){

    String url = loginGoogleService.gerarUrl();

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(url));

    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }

  @GetMapping("/auth")
  public ResponseEntity<AuthTokenResponse> loginGoogle(@RequestParam String code) {

    AuthTokenResponse token = loginGoogleService.loginGoogle(code);

    return ResponseEntity.ok(token);
  }
}
