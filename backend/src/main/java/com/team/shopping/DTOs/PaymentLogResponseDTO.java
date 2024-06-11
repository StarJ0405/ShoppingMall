package com.team.shopping.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PaymentLogResponseDTO {

    private Long paymentLogId;

    private String paymentStatus;

    private String url;

    private String productTitle;

    private int price;

    private LocalDateTime paymentDate;

    @Builder
    public PaymentLogResponseDTO (Long paymentLogId, String paymentStatus,
                                  String url, String productTitle, int price, LocalDateTime paymentDate) {
        this.paymentLogId = paymentLogId;
        this.paymentStatus = paymentStatus;
        this.url = url;
        this.productTitle = productTitle;
        this.price = price;
        this.paymentDate = paymentDate;
    }


}
