package com.syxbruno.authentication_project.repository;

import com.syxbruno.authentication_project.model.User;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findByToken(@NotBlank String code);
}
