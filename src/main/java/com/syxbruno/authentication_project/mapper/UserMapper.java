package com.syxbruno.authentication_project.mapper;

import com.syxbruno.authentication_project.dto.response.user.UserResponse;
import com.syxbruno.authentication_project.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserResponse toUserResponse(User user);
}
