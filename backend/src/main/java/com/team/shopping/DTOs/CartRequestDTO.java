package com.team.shopping.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartRequestDTO {

    private Long productId;

    private List<Long> optionIdList;

    private int count;
}
