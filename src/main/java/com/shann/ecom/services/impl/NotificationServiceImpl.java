package com.shann.ecom.services.impl;

import com.shann.ecom.exceptions.NotificationNotFoundException;
import com.shann.ecom.exceptions.ProductInStockException;
import com.shann.ecom.exceptions.ProductNotFoundException;
import com.shann.ecom.exceptions.UnAuthorizedException;
import com.shann.ecom.exceptions.UserNotFoundException;
import com.shann.ecom.models.Notification;
import com.shann.ecom.models.NotificationStatus;
import com.shann.ecom.models.User;
import com.shann.ecom.repositories.InventoryRepository;
import com.shann.ecom.repositories.NotificationRepository;
import com.shann.ecom.repositories.ProductRepository;
import com.shann.ecom.repositories.UserRepository;
import com.shann.ecom.services.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

  private NotificationRepository notificationRepository;
  private ProductRepository productRepository;
  private UserRepository userRepository;
  private InventoryRepository inventoryRepository;

  public NotificationServiceImpl(
      NotificationRepository notificationRepository,
      ProductRepository productRepository,
      UserRepository userRepository,
      InventoryRepository inventoryRepository) {
    this.notificationRepository = notificationRepository;
    this.productRepository = productRepository;
    this.userRepository = userRepository;
    this.inventoryRepository = inventoryRepository;
  }

  @Override
  public Notification registerUser(int userId, int productId)
      throws UserNotFoundException, ProductNotFoundException, ProductInStockException {
    var user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User doesn't exist"));
    var product =
        productRepository
            .findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Prodcut doesn't exist"));
    var inventoryOptional = inventoryRepository.findByProduct(product);
    if (inventoryOptional.isPresent()) {
      var inventory = inventoryOptional.get();
      if (inventory.getQuantity() > 0) throw new ProductInStockException("Product In Stock");
    }
    var notification = new Notification();
    notification.setProduct(product);
    notification.setUser(user);
    notification.setStatus(NotificationStatus.PENDING);
    return notificationRepository.save(notification);
  }

  @Override
  public void deregisterUser(int userId, int notificationId)
      throws UserNotFoundException, NotificationNotFoundException, UnAuthorizedException {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User Doesn't exist"));
    var notification =
        notificationRepository
            .findById(notificationId)
            .orElseThrow(() -> new NotificationNotFoundException("Notification doesn't exist"));
    if (notification.getUser().getId() != userId)
      throw new UnAuthorizedException("Notification doesn't belong to user");
    notificationRepository.delete(notification);
  }
}
