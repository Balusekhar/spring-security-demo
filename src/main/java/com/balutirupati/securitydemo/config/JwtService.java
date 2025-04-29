package com.balutirupati.securitydemo.config;

import com.balutirupati.securitydemo.entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String jwtSecret;

  public String generateAccessToken(UserEntity user) {
    SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

    return Jwts.builder()
      .setSubject(user.getEmail())
      .claim("id", user.getId())
      .claim("roles", user.getRoles().toString())
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 5)))
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();
  }

  public String generateRefreshToken(UserEntity user) {
    SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

    return Jwts.builder()
      .setSubject(user.getEmail())
      .claim("id", user.getId())
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24)))
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();
  }

  public UUID parseJwt(String token) {
    SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

    Claims claims = Jwts.parserBuilder()
      .setSigningKey(key)
      .build()
      .parseClaimsJws(token)
      .getBody();

    String idString = claims.get("id", String.class);
    return UUID.fromString(idString);
  }
}
