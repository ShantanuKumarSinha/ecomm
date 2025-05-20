package com.shann.ecom.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ecom_order_detail")
@Data
public class OrderDetail extends BaseModel {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", referencedColumnName = "id")
  @JsonBackReference
  private Order order;

  @ManyToOne
  @JoinColumn(name = "product_id", referencedColumnName = "id")
  private Product product;

  private int quantity;
}
