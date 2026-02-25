package com.shann.ecom.strategy;


import com.shann.ecom.models.Token;
import com.shann.ecom.models.User;

public interface TokenGenerationStrategy {
    Token generateToken(User user);
}
