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
  private HighDemandProductRepository highDemandProductRepository;

  public OrderServiceImpl(
      UserRepository userRepository,
      OrderRepository orderRepository,
      OrderDetailRepository orderDetailRepository,
      InventoryRepository inventoryRepository,
      ProductRepository productRepository,
      AddressRepository addressRepository,
      HighDemandProductRepository highDemandProductRepository) {

    this.userRepository = userRepository;
    this.orderRepository = orderRepository;
    this.orderDetailRepository = orderDetailRepository;
    this.inventoryRepository = inventoryRepository;
    this.productRepository = productRepository;
    this.addressRepository = addressRepository;
    this.highDemandProductRepository = highDemandProductRepository;
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
    if (address.getUser().getId() != userId)
      throw new InvalidAddressException("Address doesn't belong to this user");
    //    if (!user.getAddresses().contains(address) )
    //      throw new InvalidAddressException("Address doesn't belong to this user");
    var inventories = new ArrayList<Inventory>();
    var orderDetails = new ArrayList<OrderDetail>();
    var productIds = new ArrayList<Integer>();
    for (var orderPair : orderPairs) {
      var product =
          productRepository
              .findById(orderPair.getFirst())
              .orElseThrow(() -> new InvalidProductException("Product not found"));
      productIds.add(product.getId());
    }
    inventories = (ArrayList<Inventory>) inventoryRepository.findAllByProductIdIn(productIds);
    if (inventories.size() != productIds.size()) throw new OutOfStockException("Product not found");

    for (var orderPair : orderPairs) {
      var inventory =
          inventories.stream()
              .filter(inv -> inv.getQuantity() >= orderPair.getSecond())
              .findFirst()
              .orElseThrow(() -> new OutOfStockException("Not Enough Product in Stock"));
      var highDemandProductOptional = highDemandProductRepository.findById(orderPair.getFirst());
      if (highDemandProductOptional.isPresent()) {
        var highDemandProduct = highDemandProductOptional.get();
        if (orderPair.getSecond() > highDemandProduct.getMaxQuantity())
          throw new HighDemandProductException(
              "High demand product can't be ordered more than "
                  + highDemandProduct.getMaxQuantity());
      }
    }
    inventoryRepository.saveAll(inventories);
    var order = new Order();
    order.setUser(user);
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
