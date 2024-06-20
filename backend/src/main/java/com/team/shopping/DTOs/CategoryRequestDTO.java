package com.team.shopping.DTOs;

import lombok.*;

@Getter
public class CategoryRequestDTO {

    private Long id;

    private String name;

    private String parent;

    private String newName;
}

