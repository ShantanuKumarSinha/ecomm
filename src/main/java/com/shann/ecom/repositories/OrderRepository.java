package com.shann.ecom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shann.ecom.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

}
