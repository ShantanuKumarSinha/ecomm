package com.shann.ecom.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="ecom_notification")
@Data
public class Notification extends BaseModel{
    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private Product product;
    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
    @Enumerated(EnumType.ORDINAL)
    private NotificationStatus status;
}
