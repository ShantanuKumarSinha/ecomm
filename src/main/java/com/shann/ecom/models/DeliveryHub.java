package com.shann.ecom.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ecom_delivery_hub")
@Data
public class DeliveryHub extends BaseModel {
  @OneToOne
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private Address address;

  private String name;
}
