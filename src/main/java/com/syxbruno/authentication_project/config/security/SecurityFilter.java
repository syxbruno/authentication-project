package com.syxbruno.authentication_project.config.security;

import com.syxbruno.authentication_project.exception.UserResponseException;
import com.syxbruno.authentication_project.model.User;
import com.syxbruno.authentication_project.repository.UserRepository;
import com.syxbruno.authentication_project.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService service;
    private final UserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = recoverToken(request);

        if (token != null) {

            String subject = service.validateToken(token);

            User user = repository.findByEmail(subject)
                    .orElseThrow(() -> new UserResponseException("This email is not registered in the database"));

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        if (header == null) {

            return null;
        }

        return header.replace("Bearer ", "");
    }
}
