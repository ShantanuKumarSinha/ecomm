package com.shann.ecom.strategy.impl;

import com.shann.ecom.models.Token;
import com.shann.ecom.models.User;
import com.shann.ecom.strategy.TokenGenerationStrategy;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class SecureRandomTokenGenerationStrategy implements TokenGenerationStrategy {

  private static final String CHARACTERS =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

  private static final int LENGTH = 16;

  @Override
  public Token generateToken(User user) {
    StringBuilder tokenValue = new StringBuilder(LENGTH);
    SecureRandom random = new SecureRandom();
    LocalDateTime expiry = LocalDateTime.now().plusMinutes(15); // Token valid for 15 min
    tokenValue.append(user.getId());
    for (int i = 0; i < LENGTH; i++) {
      var index = random.nextInt(CHARACTERS.length());
      tokenValue.append(CHARACTERS.charAt(index));
    }
    return Token.builder().user(user).token(tokenValue.toString()).expiresAt(expiry).build();
  }
}
