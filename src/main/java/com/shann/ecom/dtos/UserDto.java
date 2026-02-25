package com.shann.ecom.dtos;

import com.shann.ecom.enums.UserType;
import com.shann.ecom.models.User;
import lombok.Data;

@Data
public class UserDto {
  private Long id;
  private String userName;
  private String email;
  private String phone;
  private UserType userType;

  public static UserDto from(User user) {
    UserDto userDto = new UserDto();
    userDto.setId((long) user.getId());
    userDto.setUserName(user.getName());
    userDto.setEmail(user.getEmail());
    userDto.setPhone(user.getPhone());
    userDto.setUserType(user.getUserType());
    return userDto;
  }

  public static User to(UserDto userDto) {
    User user = new User();
    user.setId(userDto.getId().intValue());
    user.setName(userDto.getUserName());
    user.setEmail(userDto.getEmail());
    user.setPhone(userDto.getPhone());
    user.setUserType(userDto.getUserType());
    return user;
  }
}
