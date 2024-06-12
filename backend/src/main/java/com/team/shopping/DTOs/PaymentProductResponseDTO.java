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

    private String seller;

    private int price;

    private String description;

    private String title;

    private String brand;

    private int count;

    private List<PaymentProductDetailResponseDTO> paymentProductDetailResponseDTOList;

    @Builder
    public PaymentProductResponseDTO (PaymentProduct paymentProduct,
                                      List<PaymentProductDetailResponseDTO> paymentProductDetailResponseDTOList) {
        this.productId = paymentProduct.getProductId();
        this.seller = paymentProduct.getSeller();
        this.price = paymentProduct.getPrice();
        this.description = paymentProduct.getDescription();
        this.title = paymentProduct.getTitle();
        this.brand = paymentProduct.getBrand();
        this.count = paymentProduct.getCount();
        this.paymentProductDetailResponseDTOList = paymentProductDetailResponseDTOList;

    }
}