package com.balutirupati.securitydemo.entities;

import com.balutirupati.securitydemo.enums.Permissions;
import com.balutirupati.securitydemo.enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity implements UserDetails {


  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String email;
  private String password;
  private String userName;
  private int age;

  @ElementCollection(fetch = FetchType.EAGER)
  @Enumerated(EnumType.STRING)
  private Set<Roles> roles;

  @ElementCollection(fetch = FetchType.EAGER)
  @Enumerated(EnumType.STRING)
  private Set<Permissions> permissions;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<SimpleGrantedAuthority> authorities = roles.stream()
      .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
      .collect(Collectors.toSet());
    permissions.forEach(permission ->
      authorities.add(new SimpleGrantedAuthority(permission.name())));
    return authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.userName;
  }
}
