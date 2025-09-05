package com.syxbruno.authentication_project.dto.user.request;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(@NotBlank String email, @NotBlank String password) { }