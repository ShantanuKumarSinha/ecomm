package com.shann.ecom.models;

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
    @OneToMany(mappedBy = "userInOrder", fetch = FetchType.LAZY)
    private List<Order> orders;
    @OneToMany(mappedBy = "userInAddress", fetch = FetchType.LAZY)
    private List<Address> addresses;
    @Enumerated(EnumType.ORDINAL)
    private UserType userType;
}
