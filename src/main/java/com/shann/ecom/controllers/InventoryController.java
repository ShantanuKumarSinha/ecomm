package com.shann.ecom.controllers;

import com.shann.ecom.dtos.*;
import com.shann.ecom.dtos.ResponseStatus;
import com.shann.ecom.exceptions.ProductNotFoundException;
import com.shann.ecom.exceptions.UnAuthorizedAccessException;
import com.shann.ecom.exceptions.UserNotFoundException;
import com.shann.ecom.models.Inventory;
import com.shann.ecom.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventories")
public class InventoryController {

    private InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("")
    public CreateOrUpdateResponseDto createOrUpdateInventory(@RequestBody CreateOrUpdateRequestDto requestDto){
        var createOrUpdateResponseDto = new CreateOrUpdateResponseDto();
        try{
            var inventory =  inventoryService.createOrUpdateInventory(requestDto.getUserId(), requestDto.getProductId(),requestDto.getQuantity());
            createOrUpdateResponseDto.setInventory(inventory);
            createOrUpdateResponseDto.setResponseStatus(ResponseStatus.SUCCESS);
        } catch(UserNotFoundException | UnAuthorizedAccessException | ProductNotFoundException exception){
            createOrUpdateResponseDto.setResponseStatus(ResponseStatus.FAILURE);
        }
        return createOrUpdateResponseDto;
    }

    @DeleteMapping("")
    public DeleteInventoryResponseDto deleteInventory(@RequestBody DeleteInventoryRequestDto requestDto){
        var deleteInventoryResponseDto = new DeleteInventoryResponseDto();
        try{
            inventoryService.deleteInventory(requestDto.getUserId(),requestDto.getProductId());
            deleteInventoryResponseDto.setResponseStatus(ResponseStatus.SUCCESS);
        }catch(UserNotFoundException | UnAuthorizedAccessException exception){
            deleteInventoryResponseDto.setResponseStatus(ResponseStatus.FAILURE);
        }
        return deleteInventoryResponseDto;
    }


    public UpdateInventoryResponseDto updateInventory(UpdateInventoryRequestDto requestDto) {
        UpdateInventoryResponseDto responseDto = new UpdateInventoryResponseDto();
        try{
            Inventory inventory = inventoryService.updateInventory(requestDto.getProductId(), requestDto.getQuantity());
            responseDto.setInventory(inventory);
            responseDto.setResponseStatus(ResponseStatus.SUCCESS);
            return responseDto;
        } catch (Exception e){
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
            return responseDto;
        }
    }
}
