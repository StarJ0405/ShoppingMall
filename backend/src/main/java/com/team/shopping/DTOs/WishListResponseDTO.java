package com.team.shopping.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishListResponseDTO {

    private Long id;
    private String username;
    private List<ProductResponseDTO> productList;
}
