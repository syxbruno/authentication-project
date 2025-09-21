package com.syxbruno.authentication_project.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthDataA2f(@NotBlank String email, @NotBlank String code) {

}
