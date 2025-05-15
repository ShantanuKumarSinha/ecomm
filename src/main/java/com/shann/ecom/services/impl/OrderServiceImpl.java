package com.shann.ecom.services.impl;

import com.shann.ecom.enums.OrderStatus;
import com.shann.ecom.exceptions.*;
import com.shann.ecom.models.Inventory;
import com.shann.ecom.models.Order;
import com.shann.ecom.models.OrderDetail;
import com.shann.ecom.repositories.*;
import com.shann.ecom.services.OrderService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

  private UserRepository userRepository;
  private OrderRepository orderRepository;
  private OrderDetailRepository orderDetailRepository;
  private InventoryRepository inventoryRepository;
  private ProductRepository productRepository;
  private AddressRepository addressRepository;

  public OrderServiceImpl(
      UserRepository userRepository,
      OrderRepository orderRepository,
      OrderDetailRepository orderDetailRepository,
      InventoryRepository inventoryRepository,
      ProductRepository productRepository,
      AddressRepository addressRepository) {

    this.userRepository = userRepository;
    this.orderRepository = orderRepository;
    this.orderDetailRepository = orderDetailRepository;
    this.inventoryRepository = inventoryRepository;
    this.productRepository = productRepository;
    this.addressRepository = addressRepository;
  }

  @Override
  public Order placeOrder(int userId, int addressId, List<Pair<Integer, Integer>> orderPairs)
      throws UserNotFoundException,
          InvalidAddressException,
          OutOfStockException,
          InvalidProductException,
          HighDemandProductException {
    var user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
    var address =
        addressRepository
            .findById(addressId)
            .orElseThrow(() -> new InvalidAddressException("Address not found"));
    if(address.getUserInAddress().getId() != userId)
      throw new InvalidAddressException("Address doesn't belong to this user");
//    if (!user.getAddresses().contains(address) )
//      throw new InvalidAddressException("Address doesn't belong to this user");
    var inventories = new ArrayList<Inventory>();
    var orderDetails = new ArrayList<OrderDetail>();
    for (var orderPair : orderPairs) {
      var product =
          productRepository
              .findById(orderPair.getFirst())
              .orElseThrow(() -> new InvalidProductException("Product not found"));
      var inventory =
          inventoryRepository
              .findByProduct(product)
              .orElseThrow(() -> new OutOfStockException("Inventory out of stock"));
      if (inventory.getQuantity() < orderPair.getSecond())
        throw new OutOfStockException("Inventory out of stock");
      inventory.setQuantity(inventory.getQuantity() - orderPair.getSecond());
      inventories.add(inventory);
      // create order detail
      var orderDetail = new OrderDetail();
      orderDetail.setProduct(product);
      orderDetail.setQuantity(orderPair.getSecond());
      // add order detail to order details list
      orderDetails.add(orderDetail);

    }
    inventoryRepository.saveAll(inventories);
    var order = new Order();
    order.setUserInOrder(user);
    order.setOrderStatus(OrderStatus.PLACED);
    order.setOrderDetails(orderDetails);
    return orderRepository.save(order);
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
    if (!order.getUserInOrder().equals(user))
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
