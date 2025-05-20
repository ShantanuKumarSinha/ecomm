package com.shann.ecom.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ecom_address")
@Data
public class Address extends BaseModel {
  private String building;
  private int floor;
  private String roomNo;
  private String street;
  private String city;
  private String state;
  private String country;
  private String zipCode;
  private double latitude;
  private double longitude;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @JsonBackReference
  private User user;

  @OneToOne(mappedBy = "deliveryAddress")
  @JsonBackReference
  private Order order;
}
