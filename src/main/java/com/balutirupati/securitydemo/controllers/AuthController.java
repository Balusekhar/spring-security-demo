package com.balutirupati.securitydemo.controllers;

import com.balutirupati.securitydemo.dto.LoginDto;
import com.balutirupati.securitydemo.dto.LoginResponseDto;
import com.balutirupati.securitydemo.dto.SignupDto;
import com.balutirupati.securitydemo.dto.UserDto;
import com.balutirupati.securitydemo.services.AuthService;
import com.balutirupati.securitydemo.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  private final AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<UserDto> signup(@RequestBody SignupDto signupDto) {
    UserDto userDetails = userService.signup(signupDto);
    return ResponseEntity.ok(userDetails);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
    LoginResponseDto loginResponseDto = authService.login(loginDto);
    Cookie cookie = new Cookie("token", loginResponseDto.getRefreshToken());
    cookie.setHttpOnly(true);
    cookie.setMaxAge(60 * 60 * 24);
    response.addCookie(cookie);
    return ResponseEntity.ok(loginResponseDto);
  }

  @PostMapping("/refresh")
  public ResponseEntity<LoginResponseDto> login(HttpServletRequest request) {
    String refreshToken = Arrays.stream(request.getCookies())
      .filter(cookie -> "refreshToken".equals(cookie.getName()))
      .findFirst()
      .map(cookie -> cookie.getValue())
      .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found"));
    LoginResponseDto loginResponseDto = authService.refresh(refreshToken);
    return ResponseEntity.ok(loginResponseDto);
  }
}
