package com.shann.ecom.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
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
}
