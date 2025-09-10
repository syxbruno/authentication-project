package com.syxbruno.authentication_project.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.syxbruno.authentication_project.config.properties.TokenProperties;
import com.syxbruno.authentication_project.exception.BusinessRules;
import com.syxbruno.authentication_project.model.User;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

  private final TokenProperties secret;
  private final String WITH_ISSUER = "auth-project";

  public String generateToken(User user) {

    try {

      Algorithm algorithm = Algorithm.HMAC256(secret.getSecret());

      return JWT.create()
          .withIssuer(WITH_ISSUER)
          .withSubject(user.getEmail())
          .withExpiresAt(expiryToken(60L))
          .sign(algorithm);

    } catch (JWTCreationException e) {

      throw new BusinessRules("Error creating token");
    }
  }

  public String generateRefreshToken(User user) {

    try {

      Algorithm algorithm = Algorithm.HMAC256(secret.getSecret());

      return JWT.create()
          .withIssuer(WITH_ISSUER)
          .withSubject(user.getId().toString())
          .withExpiresAt(expiryToken(120L))
          .sign(algorithm);

    } catch (JWTCreationException e) {

      throw new BusinessRules("Error creating token");
    }
  }

  public String validateToken(String token) {

    try {

      Algorithm algorithm = Algorithm.HMAC256(secret.getSecret());

      return JWT.require(algorithm)
          .withIssuer(WITH_ISSUER)
          .build()
          .verify(token)
          .getSubject();

    } catch (JWTVerificationException e) {

      return "";
    }
  }

  private Instant expiryToken(Long minutes) {

    return LocalDateTime.now().plusMinutes(minutes).toInstant(ZoneOffset.of("-03:00"));
  }
}
