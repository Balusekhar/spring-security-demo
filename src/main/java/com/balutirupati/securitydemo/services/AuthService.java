package com.balutirupati.securitydemo.services;

import com.balutirupati.securitydemo.config.JwtService;
import com.balutirupati.securitydemo.dto.LoginDto;
import com.balutirupati.securitydemo.dto.LoginResponseDto;
import com.balutirupati.securitydemo.entities.UserEntity;
import com.balutirupati.securitydemo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserService userService;
  private final SessionService sessionService;

  public LoginResponseDto login(LoginDto loginDto) {
    Optional<UserEntity> user = userRepository.findByEmail(loginDto.getEmail());
    if (user.isEmpty()) {
      throw new BadCredentialsException("User not found");
    }
    Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
      (loginDto.getEmail(), loginDto.getPassword()));

    UserEntity userEntityDetails = (UserEntity) auth.getPrincipal();
    String accessToken = jwtService.generateAccessToken(userEntityDetails);
    String refreshToken = jwtService.generateRefreshToken(userEntityDetails);
    sessionService.generateNewSession(userEntityDetails, refreshToken);
    return new LoginResponseDto(userEntityDetails.getId(), accessToken, refreshToken);
  }

  public LoginResponseDto refresh(String refreshToken) {
    UUID userId = jwtService.parseJwt(refreshToken);
    sessionService.validSession(refreshToken);
    if (userId == null) {
      throw new BadCredentialsException("Invalid refresh token");
    }
    UserEntity userEntityDetails = userService.getUserById(userId);
    String accessToken = jwtService.generateAccessToken(userEntityDetails);
    return new LoginResponseDto(userEntityDetails.getId(), accessToken, refreshToken);
  }
}
