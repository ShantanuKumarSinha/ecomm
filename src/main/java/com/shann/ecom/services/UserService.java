package com.shann.ecom.services;

import com.shann.ecom.exceptions.InvalidCredentialException;
import com.shann.ecom.exceptions.UserNotFoundException;
import com.shann.ecom.models.Token;
import com.shann.ecom.models.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User createUser(User user);
    Token signIn(User user) throws UserNotFoundException, InvalidCredentialException;
    User validateUser(Token token);


}
