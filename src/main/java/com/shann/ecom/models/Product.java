package com.shann.ecom.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "ecom_product")
@ToString(callSuper = true)
@Data
public class Product extends BaseModel {
  private String name;
  private String description;
  private double price;

  @ManyToOne
  @JoinColumn(name = "seller_id")
  private Seller seller;
}
