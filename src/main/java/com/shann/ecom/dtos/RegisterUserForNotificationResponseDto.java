package com.shann.ecom.dtos;

import com.shann.ecom.models.Notification;
import lombok.Data;

@Data
public class RegisterUserForNotificationResponseDto {
  private Notification notification;
  private ResponseStatus responseStatus;
}
