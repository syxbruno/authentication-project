package com.syxbruno.authentication_project.service;

import com.syxbruno.authentication_project.config.properties.GoogleOAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

  private final GoogleOAuthProperties properties;
  private final String callbackUri = "http://localhost:8080/login/google/auth";

  public String generateUrl() {

    return "https://accounts.google.com/o/oauth2/v2/auth" +
        "?client_id=" + properties.getId() +
        "&redirect_uri" + callbackUri +
        "&scope=https://www.googleapis.com/auth/userinfo.email" +
        "&response_type=code";
  }
}
