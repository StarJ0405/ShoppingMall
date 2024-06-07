package com.team.shopping.DTOs;

import com.team.shopping.Domains.Product;
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
    public ProductResponseDTO(Product product) {
        this.id = product.getId();
        this.authorUsername = product.getAuthor().getUsername();
        this.categoryName = product.getCategory().getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.detail = product.getDetail();
        this.dateLimit = product.getDateLimit();
        this.count = product.getCount();
        this.title = product.getTitle();
        this.delivery = product.getTitle();
        this.address = product.getAddress();
        this.receipt = product.getReceipt();
        this.a_s = product.getA_s();
        this.brand = product.getBrand();
        this.createDate = null;
    }
}
