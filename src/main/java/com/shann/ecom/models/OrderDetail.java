package com.shann.ecom.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="ecom_order_detail")
@Data
public class OrderDetail extends BaseModel{
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName="id")
    private Order order;
    @OneToOne
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private Product product;
    private int quantity;
}
