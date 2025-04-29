package com.balutirupati.securitydemo.dto;

import com.balutirupati.securitydemo.Roles;
import lombok.Data;

import java.util.Set;

@Data
public class SignupDto {

  private String email;
  private String userName;
  private String password;
  private Set<Roles> roles;
  
}

