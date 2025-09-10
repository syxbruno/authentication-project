package com.syxbruno.authentication_project.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(@NotBlank String email, @NotBlank String password) {

}