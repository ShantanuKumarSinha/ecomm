package com.shann.ecom.services;

import com.shann.ecom.exceptions.ProductNotFoundException;
import com.shann.ecom.models.Product;

import java.util.List;

public interface RecommendationsService {

    public List<Product> getRecommendations(int productId) throws ProductNotFoundException;
}
