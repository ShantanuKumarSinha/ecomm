package com.shann.ecom.controllers;

import com.shann.ecom.dtos.SingUpRequestDto;
import com.shann.ecom.dtos.UserDto;
import com.shann.ecom.exceptions.InvalidCredentialException;
import com.shann.ecom.exceptions.UserNotFoundException;
import com.shann.ecom.models.Token;
import com.shann.ecom.models.User;
import com.shann.ecom.services.UserService;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableWebSecurity
@RequestMapping("/users")
public class UserController {

  private UserService userService;
  private PasswordEncoder passwordsEncoder;

  public UserController(UserService userService, PasswordEncoder passwordsEncoder) {
    this.userService = userService;
    this.passwordsEncoder = passwordsEncoder;
  }

  @PostMapping("/signup")
  public UserDto createUser(@RequestBody SingUpRequestDto singUpRequestDto) {
    var user = new User();
    user.setEmail(singUpRequestDto.getEmail());
    user.setName(singUpRequestDto.getFirstName() + " " + singUpRequestDto.getLastName());
    user.setPassword(passwordsEncoder.encode(singUpRequestDto.getPassword()));
    user.setPhone(singUpRequestDto.getPhoneNumber());
    userService.createUser(user);
    return UserDto.from(user);
  }

  @PostMapping("/signin")
  public Token signIn(@RequestBody UserDto userDto)
      throws UserNotFoundException, InvalidCredentialException {
    return userService.signIn(UserDto.to(userDto));
  }
}
