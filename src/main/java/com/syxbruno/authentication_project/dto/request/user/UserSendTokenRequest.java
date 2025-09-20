package com.syxbruno.authentication_project.dto.request.user;

import jakarta.validation.constraints.NotBlank;

public record UserSendTokenRequest(@NotBlank String email) {

}
