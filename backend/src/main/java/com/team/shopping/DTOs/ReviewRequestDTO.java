package com.team.shopping.DTOs;

import lombok.Getter;

@Getter
public class ReviewRequestDTO {

    private Long productId;

    private String title;

    private String content;

    private Double grade;

}
