package com.shann.ecom.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;

@Entity
@Table(name ="ecom_user")
@Data
public class User extends BaseModel{
    private String name;
    private String email;
    @OneToMany
    private List<Order> orders;
    @OneToMany
    private List<Address> addresses;
    private UserType userType;
}
