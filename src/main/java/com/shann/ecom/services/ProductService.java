package com.shann.ecom.services;

import com.shann.ecom.exceptions.AddressNotFoundException;
import com.shann.ecom.exceptions.ProductNotFoundException;

import java.util.Date;

public interface ProductService {
    public Date estimateDeliveryDate(int productId, int addressId) throws ProductNotFoundException, AddressNotFoundException;
}
