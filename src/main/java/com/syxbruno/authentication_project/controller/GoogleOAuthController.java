package com.syxbruno.authentication_project.controller;

import com.syxbruno.authentication_project.service.GoogleOAuthService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/google")
public class GoogleOAuthController {

  private final GoogleOAuthService service;

  @GetMapping
  public ResponseEntity<Void> redirectGoogle() {

    String url = service.generateUrl();

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(url));

    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }
}
