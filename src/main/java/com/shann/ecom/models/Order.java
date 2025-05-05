package com.shann.ecom.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "ecom_order")
@Data
public class Order extends BaseModel{
    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
    @OneToMany
    private List<OrderDetail> orderDetails;
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus orderStatus;
}
