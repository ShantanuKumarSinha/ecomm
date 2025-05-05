package com.shann.ecom.repositories;

import com.shann.ecom.models.Notification;
import com.shann.ecom.models.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    public List<Notification> findByProduct(Product product);

}
