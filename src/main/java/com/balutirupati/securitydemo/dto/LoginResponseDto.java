package com.balutirupati.securitydemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LoginResponseDto {
  private UUID id;
  private String accessToken;
  private String refreshToken;
}
