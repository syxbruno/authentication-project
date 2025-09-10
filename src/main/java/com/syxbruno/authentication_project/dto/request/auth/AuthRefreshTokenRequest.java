package com.syxbruno.authentication_project.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthRefreshTokenRequest(@NotBlank String refreshToken) {

}
