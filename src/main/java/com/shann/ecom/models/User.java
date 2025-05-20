package com.shann.ecom.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shann.ecom.enums.UserType;
import jakarta.persistence.*;

import java.util.List;
import lombok.Data;

@Entity
@Table(name ="ecom_user")
@Data
public class User extends BaseModel{
    private String name;
    private String email;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Order> orders;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private List<Address> addresses;
    @Enumerated(EnumType.ORDINAL)
    private UserType userType;
}
