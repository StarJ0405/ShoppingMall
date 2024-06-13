package com.team.shopping.DTOs;

import lombok.Getter;

@Getter
public class ProductQARequestDTO {
    private Long productId;
    private String title;
    private String content;
    private Long productQAId;
}
