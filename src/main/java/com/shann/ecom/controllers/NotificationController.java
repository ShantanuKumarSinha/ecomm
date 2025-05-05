package com.shann.ecom.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shann.ecom.dtos.DeregisterUserForNotificationRequestDto;
import com.shann.ecom.dtos.DeregisterUserForNotificationResponseDto;
import com.shann.ecom.dtos.RegisterUserForNotificationRequestDto;
import com.shann.ecom.dtos.RegisterUserForNotificationResponseDto;
import com.shann.ecom.dtos.ResponseStatus;
import com.shann.ecom.exceptions.NotificationNotFoundException;
import com.shann.ecom.exceptions.ProductInStockException;
import com.shann.ecom.exceptions.ProductNotFoundException;
import com.shann.ecom.exceptions.UnAuthorizedException;
import com.shann.ecom.exceptions.UserNotFoundException;
import com.shann.ecom.services.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public RegisterUserForNotificationResponseDto registerUser(@RequestBody RegisterUserForNotificationRequestDto requestDto) {
        var responseDto = new RegisterUserForNotificationResponseDto();
        try{
        var notification =  notificationService.registerUser(requestDto.getUserId(), requestDto.getProductId());
        responseDto.setNotification(notification);
        responseDto.setResponseStatus(ResponseStatus.SUCCESS); 
        } catch(ProductNotFoundException| UserNotFoundException| ProductInStockException e){
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
        }
        return responseDto;
    }

    public DeregisterUserForNotificationResponseDto deregisterUser(@RequestBody DeregisterUserForNotificationRequestDto requestDto) {
        var responseDto = new DeregisterUserForNotificationResponseDto();
        try{
            notificationService.deregisterUser(requestDto.getUserId(),requestDto.getNotificationId());
            responseDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch(NotificationNotFoundException|UserNotFoundException| UnAuthorizedException exception){
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
        }
        return responseDto;
    }
}
