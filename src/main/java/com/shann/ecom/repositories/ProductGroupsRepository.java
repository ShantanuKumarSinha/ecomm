package com.shann.ecom.repositories;

import com.shann.ecom.models.Product;
import com.shann.ecom.models.ProductGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductGroupsRepository extends JpaRepository<ProductGroup,Integer> {

    public List<ProductGroup> findByProductsContaining(Product product);
}
