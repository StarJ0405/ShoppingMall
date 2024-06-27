package com.team.shopping.DTOs;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

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
    private Double discount;
    private int discountPrice;
    private String description;
    private String detail;
    private Long dateLimit;
    private int remain;
    private String title;
    private String delivery;
    private String address;
    private String receipt;
    private String a_s;
    private String brand;
    private List<OptionListResponseDTO> optionListResponseDTOList;
    private List<String> tagList;
    private Long createDate;
    private Long modifyDate;
    private String url;
    private Double grade;
    private int reviewSize;
    private Map<String , Integer> numOfGrade;

    @Builder
    public ProductResponseDTO(Product product, List<String> tagList, String url,
                              List<Review> reviewList, Double averageGrade, Map<String , Integer> numOfGrade,
                              Long dateLimit, Long createDate, Long modifyDate, Double discount,
                              int discountPrice, List<OptionListResponseDTO> optionListResponseDTOList) {


        this.id = product.getId();
        this.authorUsername = product.getSeller().getUsername();
        this.topCategoryName = product.getCategory().getParent() != null && product.getCategory().getParent().getParent() != null ? product.getCategory().getParent().getParent().getName() : null;
        this.middleCategoryName = product.getCategory().getParent() != null ? product.getCategory().getParent().getName() : null;
        this.categoryName = product.getCategory().getName();
        this.price = product.getPrice();
        this.discount = discount;
        this.discountPrice = discountPrice;
        this.description = product.getDescription();
        this.detail = product.getDetail();
        this.dateLimit = dateLimit;
        this.remain = product.getRemain();
        this.title = product.getTitle();
        this.delivery = product.getDelivery();
        this.address = product.getAddress();
        this.receipt = product.getReceipt();
        this.a_s = product.getA_s();
        this.brand = product.getBrand();
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.tagList = tagList;
        this.url = url;
        this.reviewSize = reviewList.size();
        this.grade = averageGrade;
        this.numOfGrade = numOfGrade;
        this.optionListResponseDTOList = optionListResponseDTOList;

    }
}
