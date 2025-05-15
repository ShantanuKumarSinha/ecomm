package com.shann.ecom.repositories;

import com.shann.ecom.models.HighDemandProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HighDemandProductRepository extends JpaRepository<HighDemandProduct, Integer> {}
