package com.team.shopping.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponseDTO {

    private Long id;
    private String authorUsername;
    private String categoryName;
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
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @Builder
    public ProductResponseDTO(Long id, String authorUsername, String  categoryName, int price,
                              String description, String detail, LocalDateTime dateLimit,
                              int count, String title, String delivery, String address,
                              String receipt, String a_s, String brand) {
        this.id = id;
        this.authorUsername = authorUsername;
        this.categoryName = categoryName;
        this.price = price;
        this.description = description;
        this.detail = detail;
        this.dateLimit = dateLimit;
        this.count = count;
        this.title = title;
        this.delivery = delivery;
        this.address = address;
        this.receipt = receipt;
        this.a_s = a_s;
        this.brand = brand;
        this.createDate = null;
    }
}
