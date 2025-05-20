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

  /**
   * @param userId
   * @param addressId
   * @param orderPairs
   * @return Order
   * @throws UserNotFoundException
   * @throws InvalidAddressException
   * @throws OutOfStockException
   * @throws InvalidProductException
   * @throws HighDemandProductException
   */

  @Override
  @Transactional(isolation = Isolation.SERIALIZABLE)
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
    var orderDetails = new ArrayList<OrderDetail>();
    var productIds = new ArrayList<Integer>();
    //check if product exist
    for (var orderPair : orderPairs) {
      var product =
          productRepository
              .findById(orderPair.getFirst())
              .orElseThrow(() -> new InvalidProductException("Product not found"));
      productIds.add(product.getId());
    }
    // find all the inventories for the given productIds;
    var inventories = inventoryRepository.findAllByProductIdIn(productIds);
    List<Inventory> finalInventories = new ArrayList<>();
    // if inventories size is not equal to productIds size, then product is not found
    if (inventories.size() != productIds.size()) throw new OutOfStockException("Product not found");

    for (var orderPair : orderPairs) {
      // check if product is in stock
      var inventory =
          inventories.stream()
              .filter(
                  inv ->
                      (inv.getProduct().getId() == orderPair.getFirst())
                          && (inv.getQuantity() >= orderPair.getSecond()))
              .findFirst()
              .orElseThrow(() -> new OutOfStockException("Not Enough Stock for Product found"));
      // reduce the quantity of the product in inventory
      inventory.setQuantity(inventory.getQuantity() - orderPair.getSecond());
      // update the inventory lis
      finalInventories.add(inventory);
      // check if product is high demand product
      var highDemandProductOptional = highDemandProductRepository.findById(orderPair.getFirst());
      if (highDemandProductOptional.isPresent()) {
        var highDemandProduct = highDemandProductOptional.get();
        // check if the quantity is more than the max quantity
        if (orderPair.getSecond() > highDemandProduct.getMaxQuantity())
          throw new HighDemandProductException(
              "High demand product can't be ordered more than "
                  + highDemandProduct.getMaxQuantity());
      }
    }
    var order = new Order();
    order.setUser(user);
    order.setOrderStatus(OrderStatus.PLACED);
    order.setDeliveryAddress(address);
    order = orderRepository.save(order);
    // set the order in order details
    int index =0;
    var finalOrder = order;
   for(var inventory : finalInventories){
      var orderDetail = new OrderDetail();
      orderDetail.setProduct(inventory.getProduct());
      orderDetail.setQuantity(orderPairs.get(index++).getSecond());
      orderDetail.setOrder(finalOrder);
      orderDetails.add(orderDetail);
    };
    // save the order details
    orderDetails = (ArrayList<OrderDetail>) orderDetailRepository.saveAll(orderDetails);

    // set the order details in order
    order.setOrderDetails(orderDetails);

    // save the inventories in inventory repository only if the order is placed
    inventoryRepository.saveAll(finalInventories);
    return order;
  }

    /**
     * @param orderId
     * @param userId
     * @return Order
     * @throws UserNotFoundException
     * @throws OrderNotFoundException
     * @throws OrderDoesNotBelongToUserException
     * @throws OrderCannotBeCancelledException
     */
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
