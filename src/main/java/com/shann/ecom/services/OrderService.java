package com.shann.ecom.services;

import com.shann.ecom.exceptions.OrderCannotBeCancelledException;
import com.shann.ecom.exceptions.OrderDoesNotBelongToUserException;
import com.shann.ecom.exceptions.OrderNotFoundException;
import com.shann.ecom.exceptions.UserNotFoundException;
import com.shann.ecom.models.Order;

public interface OrderService {
    public Order cancelOrder(int orderId, int userId)  throws UserNotFoundException, OrderNotFoundException, OrderDoesNotBelongToUserException, OrderCannotBeCancelledException;
}
