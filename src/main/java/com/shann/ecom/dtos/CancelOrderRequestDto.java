package com.shann.ecom.dtos;

import lombok.Data;

@Data
public class CancelOrderRequestDto {
  private int orderId;
  private int userId;
}
