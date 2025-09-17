package com.syxbruno.authentication_project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  @Column(unique = true)
  private String email;
  private String password;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "users_profiles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "profiles_id"))
  private final List<Profiles> profiles = new ArrayList<>();
  private String token;
  private LocalDateTime tokenExpiration;

  public User(String name, String email, String password, Profiles profile) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.profiles.add(profile);
  }

  public void addProfile(Profiles profile) {
    this.profiles.add(profile);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return profiles;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  public void changePassword(String encryptedPassword) {
    this.password = encryptedPassword;
  }
}
