package com.shann.ecom.controllers;

import com.shann.ecom.dtos.CancelOrderRequestDto;
import com.shann.ecom.dtos.CancelOrderResponseDto;
import com.shann.ecom.dtos.ResponseStatus;
import com.shann.ecom.exceptions.OrderCannotBeCancelledException;
import com.shann.ecom.exceptions.OrderDoesNotBelongToUserException;
import com.shann.ecom.exceptions.OrderNotFoundException;
import com.shann.ecom.exceptions.UserNotFoundException;
import com.shann.ecom.services.OrderService;
import com.shann.ecom.services.impl.OrderServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

  private OrderService orderService;

  public OrderController(OrderServiceImpl orderServiceImpl) {
    this.orderService = orderServiceImpl;
  }

  @PostMapping("")
  public CancelOrderResponseDto cancelOrder(
      @RequestBody CancelOrderRequestDto cancelOrderRequestDto) {
    var cancelOrderResponseDto = new CancelOrderResponseDto();
    try {
      var order =
          orderService.cancelOrder(
              cancelOrderRequestDto.getOrderId(), cancelOrderRequestDto.getUserId());
      cancelOrderResponseDto.setOrder(order);
      cancelOrderResponseDto.setStatus(ResponseStatus.SUCCESS);
    } catch (UserNotFoundException
        | OrderNotFoundException
        | OrderDoesNotBelongToUserException
        | OrderCannotBeCancelledException e) {
      cancelOrderResponseDto.setStatus(ResponseStatus.FAILURE);
    }
    return cancelOrderResponseDto;
  }
}
