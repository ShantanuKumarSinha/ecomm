package com.shann.ecom.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shann.ecom.models.DeliveryHub;

@Repository
public interface DeliveryHubRepository extends JpaRepository<DeliveryHub, Integer>{
    @Query("Select d from DeliveryHub d where d.address.zipCode = ?1")
    public Optional<DeliveryHub> findByZipCode(String zipCode);

}
