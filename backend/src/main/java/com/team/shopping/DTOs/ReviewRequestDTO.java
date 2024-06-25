package com.team.shopping.DTOs;

import lombok.Getter;

@Getter
public class ReviewRequestDTO {

    private Long productId;

    private Long paymentProductId;

    private Long reviewId;

    private String title;

    private String content;

    private Double grade;

}
