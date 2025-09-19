package com.syxbruno.authentication_project.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.syxbruno.authentication_project.config.properties.GoogleOAuthProperties;
import com.syxbruno.authentication_project.dto.response.auth.AuthTokenResponse;
import com.syxbruno.authentication_project.model.User;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

  private final TokenService service;
  private final RestClient restClient;
  private final GoogleOAuthProperties properties;
  private final String callbackUri = "http://localhost:8080/login/google/auth";

  public String generateUrl() {

    return "https://accounts.google.com/o/oauth2/v2/auth" +
        "?client_id=" + properties.getId() +
        "&redirect_uri=" + callbackUri +
        "&scope=https://www.googleapis.com/auth/userinfo.email" +
        "&response_type=code";
  }

  public AuthTokenResponse authUserOauth(User user) {

    UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(usernamePassword);

    String token = service.generateToken((User) usernamePassword.getPrincipal());
    String refreshToken = service.generateRefreshToken((User) usernamePassword.getPrincipal());

    return new AuthTokenResponse(token, refreshToken);
  }

  public String getEmail(String code) {

    String token = getToken(code);
    DecodedJWT decodedToken = JWT.decode(token);

    return decodedToken.getClaim("email").toString();
  }

  private String getToken(String code) {

    return restClient.post()
        .uri("https://oauth2.googleapis.com/token")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(Map.of(

            "code", code,
            "client_id", properties.getId(),
            "client_secret", properties.getSecret(),
            "redirect_uri", callbackUri,
            "grant_type", "authorization_code"
        ))
        .retrieve()
        .body(Map.class)
        .get("id_token").toString();
  }
}
