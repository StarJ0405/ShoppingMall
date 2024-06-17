package com.team.shopping.DTOs;

import lombok.Getter;

import java.util.List;

@Getter
public class CartRequestDTO {

    private Long productId;

    private List<Long> optionIdList;

    private int count;
}
