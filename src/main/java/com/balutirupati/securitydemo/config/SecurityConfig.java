package com.balutirupati.securitydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/auth/login", "/auth/signup").permitAll()
        .anyRequest().authenticated()  // <-- any other request needs authentication
      )
      .csrf(AbstractHttpConfigurer::disable)
      .headers(headers -> headers.frameOptions(frame -> frame.disable()))
      .build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  //in-memory
//    @Bean
//    UserDetailsService inMemory(){
//        UserDetails normalUser = User
//                .withUsername("balu")
//                .password(passwordEncoder().encode("1234"))
//                .roles("User")
//                .build();
//        UserDetails admin = User
//                .withUsername("raju")
//                .password(passwordEncoder().encode("1234"))
//                .roles("Admin")
//                .build();
//        return new InMemoryUserDetailsManager(normalUser,admin);
//    }


}
