package com.team.shopping.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CategoryResponseDTO {

    private Long id;

    private String parentName;

    private String name;

    private List<CategoryResponseDTO> categoryResponseDTOList;

    @Builder
    public CategoryResponseDTO(Long id, String parentName, String name, List<CategoryResponseDTO> categoryResponseDTOList) {
        this.id = id;
        this.parentName=parentName;
        this.name = name;
        this.categoryResponseDTOList = categoryResponseDTOList;
    }
}
