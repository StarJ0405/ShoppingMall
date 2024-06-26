package com.team.shopping.DTOs;

import com.team.shopping.Domains.PaymentProduct;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaymentProductResponseDTO {

    private Long productId;

    private Long paymentProductId;

    private String seller;

    private int productPrice;

    private Double discount;

    private int discountPrice;

    private String description;

    private String title;

    private String brand;

    private int count;

    private String imageUrl;

    private List<PaymentProductDetailResponseDTO> paymentProductDetailResponseDTOList;

    private int withOptionPrice;

    private Boolean reviewStatus;

    private ReviewResponseDTO reviewResponseDTO;

    @Builder
    public PaymentProductResponseDTO (PaymentProduct paymentProduct,
                                      List<PaymentProductDetailResponseDTO> paymentProductDetailResponseDTOList,
                                      int withOptionPrice, String imageUrl, int discountPrice, ReviewResponseDTO responseDTO) {
        this.productId = paymentProduct.getProductId();
        this.paymentProductId = paymentProduct.getId();
        this.seller = paymentProduct.getSeller();
        this.productPrice = paymentProduct.getPrice();
        this.withOptionPrice = withOptionPrice;
        this.description = paymentProduct.getDescription();
        this.title = paymentProduct.getTitle();
        this.brand = paymentProduct.getBrand();
        this.count = paymentProduct.getCount();
        this.discount = paymentProduct.getDiscount();
        this.discountPrice = discountPrice;
        this.paymentProductDetailResponseDTOList = paymentProductDetailResponseDTOList;
        this.imageUrl = imageUrl;
        this.reviewStatus = paymentProduct.getReviewStatus();
        this.reviewResponseDTO = responseDTO;

    }
}
