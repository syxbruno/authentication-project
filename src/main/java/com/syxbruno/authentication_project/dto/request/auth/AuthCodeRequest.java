package com.syxbruno.authentication_project.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthCodeRequest(@NotBlank String code) {

}
