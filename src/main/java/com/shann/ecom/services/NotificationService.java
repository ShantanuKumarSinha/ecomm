package com.shann.ecom.services;

import com.shann.ecom.exceptions.*;
import com.shann.ecom.models.Notification;

public interface NotificationService {

  public Notification registerUser(int userId, int productId)
      throws UserNotFoundException, ProductNotFoundException, ProductInStockException;

  public void deregisterUser(int userId, int notificationId)
      throws UserNotFoundException, NotificationNotFoundException, UnAuthorizedException;
}
