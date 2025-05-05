package com.shann.ecom.dtos;

import com.shann.ecom.models.Inventory;
import lombok.Data;

@Data
public class UpdateInventoryResponseDto {
  private Inventory inventory;
  private ResponseStatus responseStatus;
}
