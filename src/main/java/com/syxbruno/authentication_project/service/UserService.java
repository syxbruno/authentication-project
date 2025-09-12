package com.syxbruno.authentication_project.service;

import com.syxbruno.authentication_project.dto.request.user.UserProfileRequest;
import com.syxbruno.authentication_project.dto.response.user.UserResponse;
import com.syxbruno.authentication_project.email.user.UserEmailService;
import com.syxbruno.authentication_project.exception.BusinessRules;
import com.syxbruno.authentication_project.mapper.UserMapper;
import com.syxbruno.authentication_project.model.Profiles;
import com.syxbruno.authentication_project.model.User;
import com.syxbruno.authentication_project.repository.ProfilesRepository;
import com.syxbruno.authentication_project.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserMapper mapper;
  private final UserRepository userRepository;
  private final UserEmailService userEmailService;
  private final ProfilesRepository profilesRepository;

  @Transactional
  public void sendToken(String email) {

    User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessRules("Email not Found"));
    String token = UUID.randomUUID().toString();

    user.setToken(token);
    user.setTokenExpiration(LocalDateTime.now().plusMinutes(30));

    userRepository.save(user);

    userEmailService.sendEmailResetPassword(user);
  }

  @Transactional
  public UserResponse addProfile(Long id, UserProfileRequest data) {

    User user = userRepository.findById(id).orElseThrow(() -> new BusinessRules("User not found"));
    Profiles profile = profilesRepository.findByName(data.profilesName());

    user.addProfile(profile);

    return mapper.toUserResponse(user);
  }
}