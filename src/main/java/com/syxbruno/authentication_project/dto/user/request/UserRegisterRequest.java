package com.syxbruno.authentication_project.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserRegisterRequest(@NotBlank String name, @NotBlank @Email String email, @NotBlank @Min(6) String password) { }