package com.syxbruno.authentication_project.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.syxbruno.authentication_project.config.properties.TokenProperties;
import com.syxbruno.authentication_project.exception.UserResponseException;
import com.syxbruno.authentication_project.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
                    .withExpiresAt(expiryToken())
                    .sign(algorithm);

        } catch (JWTCreationException e) {

            throw new UserResponseException("Error creating token");
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

    private Instant expiryToken() {

        return LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00"));
    }
}
