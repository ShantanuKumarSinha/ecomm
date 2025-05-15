package com.shann.ecom.services;

import com.shann.ecom.exceptions.*;
import com.shann.ecom.models.Order;
import org.springframework.data.util.Pair;

import java.util.List;

public interface OrderService {

  public Order placeOrder(int userId, int addressId, List<Pair<Integer, Integer>> orderPairs) throws UserNotFoundException, InvalidAddressException, OutOfStockException, InvalidProductException, HighDemandProductException;

  public Order cancelOrder(int orderId, int userId)
      throws UserNotFoundException,
          OrderNotFoundException,
          OrderDoesNotBelongToUserException,
          OrderCannotBeCancelledException;
}
