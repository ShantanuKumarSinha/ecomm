package com.shann.ecom.services;

import com.shann.ecom.exceptions.ProductNotFoundException;
import com.shann.ecom.exceptions.UnAuthorizedAccessException;
import com.shann.ecom.exceptions.UserNotFoundException;
import com.shann.ecom.models.Inventory;

public interface InventoryService {

  public Inventory createOrUpdateInventory(int userId, int productId, int quantity)
      throws ProductNotFoundException, UserNotFoundException, UnAuthorizedAccessException;

  public void deleteInventory(int userId, int productId)
      throws UserNotFoundException, UnAuthorizedAccessException;

  public Inventory updateInventory(int productId, int quantity) throws ProductNotFoundException;
}
