package com.shann.ecom.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ecom_seller")
@Data
public class Seller extends BaseModel {
  private String name;
  private String email;

  @OneToOne
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private Address address;
}
