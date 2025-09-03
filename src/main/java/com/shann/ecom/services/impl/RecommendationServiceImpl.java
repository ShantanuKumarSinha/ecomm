package com.shann.ecom.services.impl;

import com.shann.ecom.exceptions.ProductNotFoundException;
import com.shann.ecom.models.Product;
import com.shann.ecom.repositories.ProductGroupsRepository;
import com.shann.ecom.repositories.ProductRepository;
import com.shann.ecom.services.RecommendationsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecommendationServiceImpl implements RecommendationsService {

    private ProductGroupsRepository productGroupRepository;
    private ProductRepository productRepository;

    public RecommendationServiceImpl(ProductGroupsRepository productGroupRepository, ProductRepository productRepository) {
        this.productGroupRepository = productGroupRepository;
        this.productRepository = productRepository;
    }


    @Override
    public List<Product> getRecommendations(int productId) throws ProductNotFoundException {
        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        var list = productGroupRepository.findByProductsContaining(product);
        return list.stream().flatMap(group -> group.getProducts().stream().distinct().filter(productInList -> productInList.getId()!=product.getId())).toList();
    }
}
