package com.shann.ecom.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shann.ecom.enums.OrderStatus;
import jakarta.persistence.*;
import java.util.List;
import lombok.Data;

@Entity
@Table(name = "ecom_order")
@Data
public class Order extends BaseModel {
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @JsonBackReference
  private User user;

  @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<OrderDetail> orderDetails;

  @Enumerated(EnumType.ORDINAL)
  private OrderStatus orderStatus;

  @OneToOne
  @JoinColumn(name = "delivery_address_id", referencedColumnName = "id")
  @JsonManagedReference
  private Address  deliveryAddress;

}
