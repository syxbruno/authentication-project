package com.syxbruno.authentication_project.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserAlterPasswordRequest(
    @NotBlank String code,
    @NotBlank @Size(min = 6) String newPassword,
    @NotBlank String repeatPassword
) {

}