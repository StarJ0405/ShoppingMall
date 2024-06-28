package com.team.shopping.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OptionRequestDTO {

    private String name;

    private int price;

    private int count;

}
