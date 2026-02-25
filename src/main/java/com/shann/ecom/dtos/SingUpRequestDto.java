package com.shann.ecom.dtos;

import lombok.Data;

@Data
public class SingUpRequestDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
