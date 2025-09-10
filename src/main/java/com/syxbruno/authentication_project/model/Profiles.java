package com.syxbruno.authentication_project.model;

import com.syxbruno.authentication_project.model.enums.ProfilesName;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.security.core.GrantedAuthority;

@Entity(name = "profiles")
public class Profiles implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Enumerated(EnumType.STRING)
  private ProfilesName name;

  @Override
  public String getAuthority() {
    return "ROLE_" + name;
  }
}
