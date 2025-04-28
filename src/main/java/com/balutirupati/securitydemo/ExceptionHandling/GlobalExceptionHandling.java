package com.balutirupati.securitydemo.ExceptionHandling;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandling {

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException e) {
    ApiResponse r = new ApiResponse(e.getLocalizedMessage());
    return new ResponseEntity<>(r, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(JwtException.class)
  public ResponseEntity<ApiResponse> handleJwtException(JwtException e) {
    ApiResponse r = new ApiResponse(e.getLocalizedMessage());
    return new ResponseEntity<>(r, HttpStatus.UNAUTHORIZED);
  }
}
