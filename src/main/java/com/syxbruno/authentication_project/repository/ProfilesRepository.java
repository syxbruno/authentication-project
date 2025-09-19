package com.syxbruno.authentication_project.repository;

import com.syxbruno.authentication_project.model.Profiles;
import com.syxbruno.authentication_project.model.enums.ProfilesName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilesRepository extends JpaRepository<Profiles, Long> {

  Optional<Profiles> findByName(ProfilesName profilesName);
}
