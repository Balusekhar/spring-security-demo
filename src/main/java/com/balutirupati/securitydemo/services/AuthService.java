package com.balutirupati.securitydemo.services;

import com.balutirupati.securitydemo.config.JwtService;
import com.balutirupati.securitydemo.dto.LoginDto;
import com.balutirupati.securitydemo.entities.UserEntity;
import com.balutirupati.securitydemo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public String login(LoginDto loginDto) {
    Optional<UserEntity> user = userRepository.findByEmail(loginDto.getEmail());
    if (user.isEmpty()) {
      throw new BadCredentialsException("User not found");
    }
    Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
      (loginDto.getEmail(), loginDto.getPassword()));

    UserEntity userEntityDetails = (UserEntity) auth.getPrincipal();
    return jwtService.generateJwtToken(userEntityDetails);
  }
}
