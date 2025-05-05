package com.shann.ecom.dtos;

import lombok.Data;

@Data
public class DeleteInventoryRequestDto {
  private int userId;
  private int productId;
}
