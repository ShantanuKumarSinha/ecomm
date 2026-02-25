package com.shann.ecom.services.impl;

import com.shann.ecom.exceptions.InvalidCredentialException;
import com.shann.ecom.exceptions.UserNotFoundException;
import com.shann.ecom.models.Token;
import com.shann.ecom.models.User;
import com.shann.ecom.repositories.UserRepository;
import com.shann.ecom.services.UserService;
import com.shann.ecom.strategy.TokenGenerationStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final TokenGenerationStrategy tokenGenerationStrategy;
  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(
          UserRepository userRepository, TokenGenerationStrategy tokenGenerationStrategy, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.tokenGenerationStrategy = tokenGenerationStrategy;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public User createUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public Token signIn(User user) throws UserNotFoundException, InvalidCredentialException {
    var userResult = userRepository.findByEmail(user.getEmail()).orElseThrow(UserNotFoundException::new);
    if (!passwordEncoder.matches(user.getPassword(),userResult.getPassword())) throw new InvalidCredentialException();
    return tokenGenerationStrategy.generateToken(userResult);
  }

  @Override
  public User validateUser(Token token) {
    return null;
  }
}
