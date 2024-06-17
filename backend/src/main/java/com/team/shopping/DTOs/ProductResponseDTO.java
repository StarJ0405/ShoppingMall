package com.team.shopping.DTOs;

import com.team.shopping.Domains.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponseDTO {

    private Long id;
    private String authorUsername;
    private String topCategoryName;
    private String middleCategoryName;
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
    private List<String> tagList;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private String url;

    @Builder
    public ProductResponseDTO(Product product, List<String> tagList, String url) {
        this.id = product.getId();
        this.authorUsername = product.getSeller().getUsername();
        this.topCategoryName = product.getCategory().getParent() != null && product.getCategory().getParent().getParent() != null ? product.getCategory().getParent().getParent().getName() : null;
        this.middleCategoryName = product.getCategory().getParent() != null ? product.getCategory().getParent().getName() : null;
        this.categoryName = product.getCategory().getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.detail = product.getDetail();
        this.dateLimit = product.getDateLimit();
        this.count = product.getRemain();
        this.title = product.getTitle();
        this.delivery = product.getTitle();
        this.address = product.getAddress();
        this.receipt = product.getReceipt();
        this.a_s = product.getA_s();
        this.brand = product.getBrand();
        this.createDate = product.getCreateDate();
        this.modifyDate = product.getModifyDate();
        this.tagList = tagList;
        this.url = url;
    }
}
