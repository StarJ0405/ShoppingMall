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

    private List<PaymentProductResponseDTO> paymentProductResponseDTOList;

    private int price;

    private LocalDateTime paymentDate;

    private String orderedName;

    private String phoneNumber;

    private String mainAddress;

    private String addressDetail;

    private int postNumber;

    private String deliveryMessage;

    private int  trackingNumber;

    @Builder
    public PaymentLogResponseDTO (int price, PaymentLog paymentLog,
                                  List<PaymentProductResponseDTO> paymentProductResponseDTOList) {
        this.paymentLogId = paymentLog.getId();
        this.paymentStatus = paymentLog.getPaymentStatus().getStatus();
        this.orderedName = paymentLog.getOrderedName();
        this.price = price;
        this.paymentDate = paymentLog.getCreateDate();
        this.paymentProductResponseDTOList = paymentProductResponseDTOList;
        this.orderedName = paymentLog.getOrderedName();
        this.phoneNumber = paymentLog.getPhoneNumber();
        this.mainAddress = paymentLog.getMainAddress();
        this.addressDetail = paymentLog.getAddressDetail();
        this.postNumber = paymentLog.getPostNumber();
        this.deliveryMessage = paymentLog.getDeliveryMessage();
        this.trackingNumber = paymentLog.getTrackingNumber();
    }


}
