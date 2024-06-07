package com.team.shopping.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductCreateRequestDTO {
    private String category;
    private int price;
    private String description;
    private String detail;
    private LocalDateTime dateLimit;
    private int count;
    private String title;
    private String delivery;
    private String address;
    private String receipt;
    private String a_s;
    private String brand;
    private List<String> productTagList;

}
