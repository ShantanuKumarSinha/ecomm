package com.shann.ecom.controllers;

import com.shann.ecom.dtos.*;
import com.shann.ecom.exceptions.*;
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

  @PostMapping("/place")
  public PlaceOrderResponseDto placeOrder(@RequestBody PlaceOrderRequestDto placeOrderRequestDto) {
    var placeOrderResponseDto = new PlaceOrderResponseDto();
    try {
      var order =
          orderService.placeOrder(
              placeOrderRequestDto.getUserId(),
              placeOrderRequestDto.getAddressId(),
              placeOrderRequestDto.getOrderPairs());
      placeOrderResponseDto.setOrder(order);
      placeOrderResponseDto.setStatus(ResponseStatus.SUCCESS);
    } catch (UserNotFoundException
        | InvalidAddressException
        | OutOfStockException
        | InvalidProductException
        | HighDemandProductException e) {
      placeOrderResponseDto.setStatus(ResponseStatus.FAILURE);
    }
    return placeOrderResponseDto;
  }

  @PostMapping("/cancel")
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
