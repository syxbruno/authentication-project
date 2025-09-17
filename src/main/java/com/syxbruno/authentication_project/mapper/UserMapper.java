package com.syxbruno.authentication_project.mapper;

import com.syxbruno.authentication_project.dto.response.user.UserResponse;
import com.syxbruno.authentication_project.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public UserResponse toUserResponse(User user) {

    return UserResponse.builder()
        .name(user.getName())
        .email(user.getEmail())
        .profiles(user.getProfiles())
        .build();
  }
}
