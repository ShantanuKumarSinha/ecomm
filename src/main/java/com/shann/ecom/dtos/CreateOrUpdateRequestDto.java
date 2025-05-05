package com.shann.ecom.dtos;

import lombok.Data;

@Data
public class CreateOrUpdateRequestDto {
    private int userId;
    private int productId;
    private int quantity;
}
