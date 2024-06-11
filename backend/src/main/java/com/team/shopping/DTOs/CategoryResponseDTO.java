package com.team.shopping.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private List<CategoryResponseDTO> categoryResponseDTOList;

    public CategoryResponseDTO(Long id, String name, List<CategoryResponseDTO> categoryResponseDTOList) {
        this.id = id;
        this.name = name;
        this.categoryResponseDTOList = categoryResponseDTOList;
    }
}
