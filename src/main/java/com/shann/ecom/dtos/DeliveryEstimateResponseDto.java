package com.shann.ecom.dtos;

import java.util.Date;
import lombok.Data;

@Data
public class DeliveryEstimateResponseDto {
    private Date expectedDeliveryDate;
    private ResponseStatus responseStatus;
}
