package com.shann.ecom.repositories;

import com.shann.ecom.models.Inventory;
import com.shann.ecom.models.Product;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

  public Optional<Inventory> findByProductId(Integer productId);

  @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "500")})
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Transactional
  List<Inventory> findAllByProductIdIn(List<Integer> ids);

  @EntityGraph(attributePaths = "product")
  Optional<Inventory> findByProduct_Id(int id);

  Optional<Inventory> findByProduct(Product product);
}
