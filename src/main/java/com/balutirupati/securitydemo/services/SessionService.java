package com.balutirupati.securitydemo.services;

import com.balutirupati.securitydemo.entities.SessionEntity;
import com.balutirupati.securitydemo.entities.UserEntity;
import com.balutirupati.securitydemo.repositories.SessionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

  private final SessionRepo sessionRepo;
  private final int SESSION_LIMIT = 5;

  public void generateNewSession(UserEntity user, String refreshToken) {
    List<SessionEntity> userSession = sessionRepo.findByUser(user);
    if (userSession.size() == SESSION_LIMIT) {
      userSession.sort(Comparator.comparing(SessionEntity::getLastUsedAt));

      SessionEntity leastRecentlyUsedSession = userSession.get(0);
      sessionRepo.delete(leastRecentlyUsedSession);
    }

    SessionEntity newSession = SessionEntity
      .builder()
      .user(user)
      .refreshToken(refreshToken)
      .build();
    sessionRepo.save(newSession);
  }

  public void validSession(String refreshToken) {
    SessionEntity session = sessionRepo.findByRefreshToken(refreshToken)
      .orElseThrow(() -> new SessionAuthenticationException("Sessoin not found for refresh token: " + refreshToken));

    session.setLastUsedAt(LocalDateTime.now());

    sessionRepo.save(session);
  }
}
