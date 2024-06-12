package com.team.shopping.DTOs;

import com.team.shopping.Domains.PaymentProductDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentProductDetailResponseDTO {

    private String optionName;

    private int optionCount;

    private int optionPrice;

    @Builder
    public PaymentProductDetailResponseDTO(PaymentProductDetail paymentProductDetail) {
        this.optionName = paymentProductDetail.getOptionName();
        this.optionCount = paymentProductDetail.getOptionCount();
        this.optionPrice = paymentProductDetail.getOptionPrice();
    }

}
