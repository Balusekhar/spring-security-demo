package com.balutirupati.securitydemo.dto;

import com.balutirupati.securitydemo.enums.Permissions;
import com.balutirupati.securitydemo.enums.Roles;
import lombok.Data;

import java.util.Set;

@Data
public class SignupDto {

  private String email;
  private String userName;
  private String password;
  private Set<Roles> roles;
  private Set<Permissions> permissions;

}

