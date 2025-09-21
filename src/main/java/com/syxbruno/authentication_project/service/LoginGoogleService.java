package com.syxbruno.authentication_project.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.syxbruno.authentication_project.config.properties.GoogleOAuthProperties;
import com.syxbruno.authentication_project.dto.response.auth.AuthTokenResponse;
import com.syxbruno.authentication_project.model.User;
import com.syxbruno.authentication_project.repository.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class LoginGoogleService {

  private final RestClient restClient;
  private final TokenService tokenService;
  private final UserRepository userRepository;
  private final GoogleOAuthProperties properties;
  private final String uriCallback = "http://localhost:8080/login/google/auth";

  public String gerarUrl() {

    return "https://accounts.google.com/o/oauth2/v2/auth" +
        "?client_id=" + properties.getId() +
        "&redirect_uri=" + uriCallback +
        "&scope=https://www.googleapis.com/auth/userinfo.email" +
        "&response_type=code";
  }

  public AuthTokenResponse loginGoogle(String code) {

    String tokenEncoded = getToken(code);
    DecodedJWT decodedJWT = JWT.decode(tokenEncoded);
    String email = decodedJWT.getClaim("email").asString();

    User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email not found"));

    String token = tokenService.generateToken(user);
    String refreshToken = tokenService.generateRefreshToken(user);

    return new AuthTokenResponse(token, refreshToken, false);
  }

  private String getToken(String code) {

    Map response = restClient.post()
        .uri("https://oauth2.googleapis.com/token")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Map.of(

            "code", code,
            "client_id", properties.getId(),
            "client_secret", properties.getSecret(),
            "redirect_uri", uriCallback,
            "grant_type", "authorization_code"
        ))
        .retrieve()
        .body(Map.class);

    return response.get("id_token").toString();
  }
}
