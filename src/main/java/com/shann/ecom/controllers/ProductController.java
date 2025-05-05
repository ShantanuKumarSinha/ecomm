package com.shann.ecom.controllers;

import com.shann.ecom.dtos.DeliveryEstimateRequestDto;
import com.shann.ecom.dtos.DeliveryEstimateResponseDto;
import com.shann.ecom.dtos.ResponseStatus;
import com.shann.ecom.exceptions.AddressNotFoundException;
import com.shann.ecom.exceptions.ProductNotFoundException;
import com.shann.ecom.services.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

  private ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping("")
  public DeliveryEstimateResponseDto estimateDeliveryTime(DeliveryEstimateRequestDto requestDto) {
    var responseDto = new DeliveryEstimateResponseDto();
    try {
      var estimatedDate =
          productService.estimateDeliveryDate(requestDto.getProductId(), requestDto.getAddressId());
      responseDto.setExpectedDeliveryDate(estimatedDate);
      responseDto.setResponseStatus(ResponseStatus.SUCCESS);
    } catch (ProductNotFoundException | AddressNotFoundException exception) {
      responseDto.setResponseStatus(ResponseStatus.FAILURE);
    }
    return responseDto;
  }
}
