package com.vp.applifting.repositories;

import com.vp.applifting.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByAccessToken(String accessToken);
  User findByUsername(String name);
}
