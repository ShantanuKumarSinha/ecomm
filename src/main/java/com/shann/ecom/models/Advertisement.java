package com.shann.ecom.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ecom_advertisement")
@Data
public class Advertisement extends BaseModel {
  private String name;
  private String description;
  @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinColumn(name = "preference_id", referencedColumnName = "id")
  private Preference preference;
}
