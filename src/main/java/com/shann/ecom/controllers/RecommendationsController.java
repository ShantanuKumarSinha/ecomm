package com.shann.ecom.controllers;

import com.shann.ecom.dtos.GenerateRecommendationsRequestDto;
import com.shann.ecom.dtos.GenerateRecommendationsResponseDto;
import com.shann.ecom.dtos.ResponseStatus;
import com.shann.ecom.exceptions.ProductNotFoundException;
import com.shann.ecom.services.RecommendationsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommendations")
public class RecommendationsController {

    private RecommendationsService recommendationsService;

    public RecommendationsController(RecommendationsService recommendationService) {
        this.recommendationsService = recommendationService;
    }

    @PostMapping("")
    public GenerateRecommendationsResponseDto generateRecommendations(@RequestBody  GenerateRecommendationsRequestDto requestDto) {
        var responseDto = new GenerateRecommendationsResponseDto();
        try{
            var products = recommendationsService.getRecommendations(requestDto.getProductId());
            responseDto.setRecommendations(products);
            responseDto.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (ProductNotFoundException e) {
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
        }
        return responseDto;
    }
}
