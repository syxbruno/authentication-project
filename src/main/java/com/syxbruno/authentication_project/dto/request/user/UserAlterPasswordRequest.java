package com.syxbruno.authentication_project.dto.request.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserAlterPasswordRequest(
    @NotBlank String code,
    @NotBlank @Min(6) String newPassword,
    @NotBlank String repeatPassword
) {

}