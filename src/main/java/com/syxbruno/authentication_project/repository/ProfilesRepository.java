package com.syxbruno.authentication_project.repository;

import com.syxbruno.authentication_project.model.Profiles;
import com.syxbruno.authentication_project.model.enums.ProfilesName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilesRepository extends JpaRepository<Profiles, Long> {

  Profiles findByName(ProfilesName profilesName);
}
