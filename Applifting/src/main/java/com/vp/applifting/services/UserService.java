package com.vp.applifting.services;

import com.vp.applifting.models.User;
import com.vp.applifting.repositories.UserRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
    return new org.springframework.security.core.userdetails.User(user.getUsername(), "", new ArrayList<>());
  }

  public Optional<UserDetails> loadUserByAccessToken(String accessToken) {
    Optional<User> userOpt = userRepository.findByAccessToken(accessToken);
    if (userOpt.isPresent()) {
      return userOpt.map(CustomUserDetails::new);
    } else {
      return Optional.empty();
    }
  }
}
