package com.team.shopping.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ProductCreateRequestDTO {

    private Long categoryId;
    private int price;
    private String description;
    private String detail;
    private LocalDateTime dateLimit;
    private int remain;
    private String title;
    private String delivery;
    private String address;
    private String receipt;
    private String a_s;
    private String brand;
    private List<String> tagList;
    private String url;
    private List<OptionListRequestDTO> optionLists;
}
