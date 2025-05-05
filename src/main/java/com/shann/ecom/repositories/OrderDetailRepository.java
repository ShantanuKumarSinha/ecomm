package com.shann.ecom.repositories;

import com.shann.ecom.models.Order;
import com.shann.ecom.models.OrderDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>{

    List<OrderDetail> findByOrder(Order order);
}