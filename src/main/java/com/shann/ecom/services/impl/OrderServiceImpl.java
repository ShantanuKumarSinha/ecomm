package com.shann.ecom.services.impl;

import com.shann.ecom.exceptions.OrderCannotBeCancelledException;
import com.shann.ecom.exceptions.OrderDoesNotBelongToUserException;
import com.shann.ecom.exceptions.OrderNotFoundException;
import com.shann.ecom.exceptions.UserNotFoundException;
import com.shann.ecom.models.Order;
import com.shann.ecom.models.OrderStatus;
import com.shann.ecom.repositories.*;
import com.shann.ecom.services.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

  private UserRepository userRepository;
  private OrderRepository orderRepository;
  private OrderDetailRepository orderDetailRepository;
  private InventoryRepository inventoryRepository;
  private ProductRepository productRepository;

  public OrderServiceImpl(
      UserRepository userRepository,
      OrderRepository orderRepository,
      OrderDetailRepository orderDetailRepository,
      InventoryRepository inventoryRepository,
      ProductRepository productRepository) {

    this.userRepository = userRepository;
    this.orderRepository = orderRepository;
    this.orderDetailRepository = orderDetailRepository;
    this.inventoryRepository = inventoryRepository;
    this.productRepository = productRepository;
  }

  @Override
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public Order cancelOrder(int orderId, int userId)
      throws UserNotFoundException,
          OrderNotFoundException,
          OrderDoesNotBelongToUserException,
          OrderCannotBeCancelledException {
    var user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
    var order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("Order doesn't exist"));
    if (!order.getUser().equals(user))
      throw new OrderDoesNotBelongToUserException("Order doesn't belong to this user");
    if (List.of(OrderStatus.CANCELLED, OrderStatus.SHIPPED, OrderStatus.DELIVERED)
        .contains(order.getOrderStatus()))
      throw new OrderCannotBeCancelledException("Order can't be cancelled");
    order.setOrderStatus(OrderStatus.CANCELLED);
    order = orderRepository.save(order);
    var orderDetails = orderDetailRepository.findByOrder(order);
    orderDetails.forEach(
        orderDetail -> {
          var inventory = inventoryRepository.findByProduct(orderDetail.getProduct()).get();
          inventory.setQuantity(inventory.getQuantity() + orderDetail.getQuantity());
          inventoryRepository.save(inventory);
        });

    return order;
  }
}
