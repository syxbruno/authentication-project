package com.syxbruno.authentication_project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @GetMapping("/student")
  public ResponseEntity<String> helloStudent() {

    return ResponseEntity.ok("Hello Student");
  }

  @GetMapping("/teacher")
  public ResponseEntity<String> helloTeacher() {

    return ResponseEntity.ok("Hello Teacher");
  }

  @GetMapping("/director")
  public ResponseEntity<String> helloDirector() {

    return ResponseEntity.ok("Hello Director");
  }
}
