package com.syxbruno.authentication_project.dto.request.user;

import com.syxbruno.authentication_project.model.enums.ProfilesName;
import jakarta.validation.constraints.NotNull;

public record UserProfileRequest(@NotNull ProfilesName profilesName) {

}
