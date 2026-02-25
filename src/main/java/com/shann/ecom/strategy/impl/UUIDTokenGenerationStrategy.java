package com.shann.ecom.strategy.impl;

import com.shann.ecom.models.Token;
import com.shann.ecom.models.User;
import com.shann.ecom.strategy.TokenGenerationStrategy;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class UUIDTokenGenerationStrategy implements TokenGenerationStrategy {

  @Override
  public Token generateToken(User user) {
    LocalDateTime expiry = LocalDateTime.now().plusMinutes(15); // Token valid for 15 min
    return Token.builder()
        .user(user)
        .token(user.getId() + "_" + UUID.randomUUID())
        .expiresAt(expiry)
        .build();
  }
}
