package com.balutirupati.securitydemo.repositories;

import com.balutirupati.securitydemo.entities.SessionEntity;
import com.balutirupati.securitydemo.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepo extends JpaRepository<SessionEntity, Long> {
  List<SessionEntity> findByUser(UserEntity user);

  Optional<SessionEntity> findByRefreshToken(String refreshToken);
}
