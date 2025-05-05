package com.shann.ecom.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ecom_product")
@Data
public class Product extends BaseModel {
  private String name;
  private String description;
  private double price;

  @ManyToOne
  @JoinColumn(name = "seller_id")
  private Seller seller;
}
