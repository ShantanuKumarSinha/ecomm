package com.shann.ecom.dtos;

import com.shann.ecom.models.Order;
import lombok.Data;

@Data
public class CancelOrderResponseDto {
    private ResponseStatus status;
    private Order order;
}
