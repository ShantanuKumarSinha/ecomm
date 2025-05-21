package com.shann.ecom.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "ecom_preference")
@Data
public class Preference extends BaseModel{
    private String category;
    private String description;
    private Date createdAt;
}
