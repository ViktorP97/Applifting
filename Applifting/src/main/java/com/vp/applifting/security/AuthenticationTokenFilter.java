package com.vp.applifting.security;

import com.vp.applifting.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

  private final UserService userService;

  @Autowired
  public AuthenticationTokenFilter(UserService userService) {
    this.userService = userService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String header = request.getHeader("Authorization");
    String accessToken = null;

    if (header != null && header.startsWith("Bearer ")) {
      accessToken = header.substring(7);
    }

    if (accessToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      Optional<UserDetails> userDetailsOpt = this.userService.loadUserByAccessToken(accessToken);

      if (userDetailsOpt.isPresent()) {
        UserDetails userDetails = userDetailsOpt.get();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }
    filterChain.doFilter(request, response);
  }
}
