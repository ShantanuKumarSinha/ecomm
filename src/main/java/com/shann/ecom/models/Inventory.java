package com.shann.ecom.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ecom_inventory")
@Data
public class Inventory extends BaseModel {
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "product_id", referencedColumnName = "id")
  private Product product;

  private int quantity;
}
