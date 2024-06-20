package com.team.shopping.DTOs;

import lombok.Getter;

import java.util.List;

@Getter
public class OptionListRequestDTO {

    private String name;

    private List<OptionRequestDTO> child;
}
