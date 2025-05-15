package com.shann.ecom.dtos;

import com.shann.ecom.models.Order;
import lombok.Data;

@Data
public class PlaceOrderResponseDto {
    private Order order;
    private ResponseStatus status;
}
