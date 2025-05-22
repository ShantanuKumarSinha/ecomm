package com.shann.ecom.controllers;

import com.shann.ecom.dtos.GetAdvertisementForUserRequestDto;
import com.shann.ecom.dtos.GetAdvertisementForUserResponseDto;
import com.shann.ecom.dtos.ResponseStatus;
import com.shann.ecom.services.AdsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Controller for handling advertisement-related requests. */
@RestController
@RequestMapping("/ads")
public class AdsController {

  /** Service for handling advertisement-related operations. */
  private AdsService adsService;

  /**
   * Constructor for AdsController.
   *
   * @param adsService the service for handling advertisement-related operations
   */
  public AdsController(AdsService adsService) {
    this.adsService = adsService;
  }

  /**
   * Retrieves an advertisement for a specific user.
   *
   * @param requestDto the request DTO containing user ID
   * @return the response DTO containing advertisement details
   */
  @GetMapping("")
  public GetAdvertisementForUserResponseDto getAdvertisementForUser(
      @RequestBody GetAdvertisementForUserRequestDto requestDto) {
    var responseDto = new GetAdvertisementForUserResponseDto();
    try {
      var advertisement = adsService.getAdvertisementForUser(requestDto.getUserId());
      responseDto.setAdvertisement(advertisement);
      responseDto.setResponseStatus(ResponseStatus.SUCCESS);
    } catch (Exception e) {
      responseDto.setResponseStatus(ResponseStatus.FAILURE);
    }
    return responseDto;
  }
}
