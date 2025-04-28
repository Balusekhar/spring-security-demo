package com.balutirupati.securitydemo.services;

import com.balutirupati.securitydemo.dto.SignupDto;
import com.balutirupati.securitydemo.dto.UserDto;
import com.balutirupati.securitydemo.entities.UserEntity;
import com.balutirupati.securitydemo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;


  public UserDto signup(SignupDto signupDto) {
    Optional<UserEntity> user = userRepository.findByEmail(signupDto.getEmail());
    if (user.isPresent()) {
      throw new BadCredentialsException("User already exists");
    }

    UserEntity userToCreate = modelMapper.map(signupDto, UserEntity.class);
    userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));

    UserEntity createdUser = userRepository.save(userToCreate);

    return modelMapper.map(createdUser, UserDto.class);
  }

  public UserEntity getUserById(UUID userId) {
    return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByEmail(username).orElseThrow(() -> new BadCredentialsException("User not found"));
  }
}
