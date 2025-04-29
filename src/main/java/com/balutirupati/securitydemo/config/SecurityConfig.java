package com.balutirupati.securitydemo.config;

import com.balutirupati.securitydemo.filters.JwtFilter;
import com.balutirupati.securitydemo.handlers.Oauth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

  private final JwtFilter jwtFilter;
  private final Oauth2SuccessHandler oauth2SuccessHandler;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/auth/login", "/auth/signup").permitAll()
        .anyRequest().authenticated()
      )
      .csrf(AbstractHttpConfigurer::disable)
      .sessionManagement(session -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .headers(headers -> headers.frameOptions(frame -> frame.disable()))
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
      .oauth2Login(
        oauthConfig -> oauthConfig
          .failureUrl("/auth/login?error=true")
          .successHandler(oauth2SuccessHandler)
      )
      .build();
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
