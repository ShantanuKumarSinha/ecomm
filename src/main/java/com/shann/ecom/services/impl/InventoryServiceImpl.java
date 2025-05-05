package com.shann.ecom.services.impl;

import com.shann.ecom.adapter.EmailAdapter;
import com.shann.ecom.adapter.MailAdapter;
import com.shann.ecom.exceptions.ProductNotFoundException;
import com.shann.ecom.exceptions.UnAuthorizedAccessException;
import com.shann.ecom.exceptions.UserNotFoundException;
import com.shann.ecom.models.Inventory;
import com.shann.ecom.models.NotificationStatus;
import com.shann.ecom.models.Product;
import com.shann.ecom.models.UserType;
import com.shann.ecom.repositories.InventoryRepository;
import com.shann.ecom.repositories.NotificationRepository;
import com.shann.ecom.repositories.ProductRepository;
import com.shann.ecom.repositories.UserRepository;
import com.shann.ecom.services.InventoryService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private InventoryRepository inventoryRepository;
    private NotificationRepository notificationRepository;
    private MailAdapter mailAdapter;

    @Autowired
    public InventoryServiceImpl(UserRepository userRepository, InventoryRepository inventoryRepository, ProductRepository productRepository, NotificationRepository notificationRepository, EmailAdapter emailAdapter) {
        this.userRepository = userRepository;
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.notificationRepository = notificationRepository;
        this.mailAdapter = emailAdapter;
    }

    @Override
    public Inventory createOrUpdateInventory(int userId, int productId, int quantity)
            throws ProductNotFoundException, UserNotFoundException, UnAuthorizedAccessException {
        var inventory = new Inventory();
        var user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User with: "+userId+" not found"));
        if(user.getUserType().equals(UserType.CUSTOMER))
            throw new UnAuthorizedAccessException("Customers can't create or update Inventory");
        var product = productRepository.findById(productId).orElseThrow(()-> new ProductNotFoundException("Product with: "+productId+" doesn't exist"));
        var optionalInventory = inventoryRepository.findByProductId(productId);
        if(optionalInventory.isPresent()){
            var inventoryResult = optionalInventory.get();
            inventory.setId(inventoryResult.getId());
            inventory.setProduct(inventoryResult.getProduct());
            inventory.setQuantity(inventoryResult.getQuantity()+quantity);
        } else {
            inventory.setProduct(product);
            inventory.setQuantity(quantity);
        }
        return inventoryRepository.save(inventory);
    }

    @Override
    public void deleteInventory(int userId, int productId) throws UserNotFoundException, UnAuthorizedAccessException {
        var user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User with: "+userId+" not found"));
        if(user.getUserType().equals(UserType.CUSTOMER))
            throw new UnAuthorizedAccessException("Customers can't delete Inventory");
        inventoryRepository.findByProductId(productId).ifPresent(inventory-> inventoryRepository.delete(inventory));
    }

    @Override
    public Inventory updateInventory(int productId, int quantity) throws ProductNotFoundException {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found"));
        Optional<Inventory> inventoryOptional = this.inventoryRepository.findByProduct_Id(product.getId());
        Inventory inventory;
        if(inventoryOptional.isEmpty()){
            inventory = new Inventory();
            inventory.setProduct(product);
            inventory.setQuantity(quantity);
            return this.inventoryRepository.save(inventory);
        }
        inventory = inventoryOptional.get();
        inventory.setQuantity(inventory.getQuantity() + quantity);
        var notifications = notificationRepository.findByProduct(product);
        notifications.forEach(notification ->{
            var user = notification.getUser();
            mailAdapter.sendMail(user.getEmail(), user.getName(), product.getName());
            notification.setStatus(NotificationStatus.SENT);
            notificationRepository.save(notification);
        });

        return this.inventoryRepository.save(inventory);
    }
}
