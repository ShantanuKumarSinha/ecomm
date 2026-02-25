package com.shann.ecom.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "ecom_token")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token extends BaseModel {

  private String token;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  private LocalDateTime expiresAt;
}
