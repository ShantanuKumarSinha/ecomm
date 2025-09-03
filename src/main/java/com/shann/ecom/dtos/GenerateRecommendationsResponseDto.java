package com.shann.ecom.dtos;

import com.shann.ecom.models.Product;
import lombok.Data;

import java.util.List;

@Data
public class GenerateRecommendationsResponseDto {

    private List<Product> recommendations;
    private ResponseStatus responseStatus;
}
