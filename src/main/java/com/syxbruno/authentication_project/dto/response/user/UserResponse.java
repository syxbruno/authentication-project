package com.syxbruno.authentication_project.dto.response.user;

import com.syxbruno.authentication_project.model.Profiles;
import java.util.List;

public record UserResponse(String name, String email, List<Profiles> profiles) {

}
