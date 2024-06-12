package com.team.shopping.DTOs;

import com.team.shopping.Domains.PaymentLog;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaymentLogResponseDTO {

    private Long paymentLogId;

    private String paymentStatus;

    private String order;

    private List<PaymentProductResponseDTO> paymentProductResponseDTOList;

    private int price;

    private LocalDateTime paymentDate;

    @Builder
    public PaymentLogResponseDTO (int price, PaymentLog paymentLog,
                                  List<PaymentProductResponseDTO> paymentProductResponseDTOList) {
        this.paymentLogId = paymentLog.getId();
        this.paymentStatus = paymentLog.getPaymentStatus().toString();
        this.order = paymentLog.getOrder().getName();
        this.price = price;
        this.paymentDate = paymentLog.getCreateDate();
        this.paymentProductResponseDTOList = paymentProductResponseDTOList;
    }


}
