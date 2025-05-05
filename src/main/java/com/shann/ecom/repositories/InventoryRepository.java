package com.shann.ecom.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.shann.ecom.models.Inventory;
import com.shann.ecom.models.Product;

import jakarta.persistence.LockModeType;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer>  {

    public Optional<Inventory> findByProductId(Integer productId);

    //@Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Inventory> findByProduct(Product product);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    Inventory save(Inventory inventory);

    @EntityGraph(attributePaths = "product")
    Optional<Inventory> findByProduct_Id(int id);
}
