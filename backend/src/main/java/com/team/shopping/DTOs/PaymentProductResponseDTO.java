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

    private int productPrice;

    private String description;

    private String title;

    private String brand;

    private int count;

    private List<PaymentProductDetailResponseDTO> paymentProductDetailResponseDTOList;

    private int withOptionPrice;

    @Builder
    public PaymentProductResponseDTO (PaymentProduct paymentProduct,
                                      List<PaymentProductDetailResponseDTO> paymentProductDetailResponseDTOList,
                                      int withOptionPrice) {
        this.productId = paymentProduct.getProductId();
        this.seller = paymentProduct.getSeller();
        this.productPrice = paymentProduct.getPrice();
        this.withOptionPrice = withOptionPrice;
        this.description = paymentProduct.getDescription();
        this.title = paymentProduct.getTitle();
        this.brand = paymentProduct.getBrand();
        this.count = paymentProduct.getCount();
        this.paymentProductDetailResponseDTOList = paymentProductDetailResponseDTOList;

    }
}
