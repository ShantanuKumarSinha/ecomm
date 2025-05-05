package com.shann.ecom.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shann.ecom.models.Order;
import com.shann.ecom.models.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>{

    List<OrderDetail> findByOrder(Order order);
}