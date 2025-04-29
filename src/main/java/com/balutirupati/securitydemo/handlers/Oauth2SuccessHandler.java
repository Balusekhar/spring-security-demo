package com.balutirupati.securitydemo.handlers;

import com.balutirupati.securitydemo.config.JwtService;
import com.balutirupati.securitydemo.entities.UserEntity;
import com.balutirupati.securitydemo.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final UserService userService;
  private final JwtService jwtService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
    DefaultOAuth2User oauthUser = (DefaultOAuth2User) oauthToken.getPrincipal();

    String email = oauthUser.getAttribute("email");
    UserEntity user = userService.getUserByEmail(email);

    if (user == null) {
      UserEntity newUser = UserEntity.builder()
        .userName(oauthUser.getAttribute("name"))
        .email(email)
        .build();
      user = userService.saveUser(newUser);
    }
    String accessToken = jwtService.generateAccessToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);

    Cookie cookie = new Cookie("token", refreshToken);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(60 * 60 * 24);
    response.addCookie(cookie);

    String redirectUrl = "http://localhost:3000/dashboard?token=" + accessToken;
    getRedirectStrategy().sendRedirect(request, response, redirectUrl);
  }
}
