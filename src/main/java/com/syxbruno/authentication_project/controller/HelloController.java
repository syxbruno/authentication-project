package com.syxbruno.authentication_project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @GetMapping("/user")
  public ResponseEntity<String> helloUser() {

    return ResponseEntity.ok("Hello User");
  }

  @GetMapping("/admin")
  public ResponseEntity<String> helloAdmin() {

    return ResponseEntity.ok("Hello Admin");
  }
}
