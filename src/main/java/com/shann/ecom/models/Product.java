package com.shann.ecom.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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
