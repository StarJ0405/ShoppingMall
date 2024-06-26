package com.team.shopping.DTOs;

import com.team.shopping.Domains.PaymentLog;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaymentLogResponseDTO {

    private Long paymentLogId;

    private String paymentStatus;

    private List<PaymentProductResponseDTO> paymentProductResponseDTOList;

    private Long totalPrice;

    private Long paymentDate;

    private String recipient;

    private String phoneNumber;

    private String mainAddress;

    private String addressDetail;

    private int postNumber;

    private String deliveryMessage;

    private int trackingNumber;

    private Long usedPoint;

    @Builder
    public PaymentLogResponseDTO (Long totalPrice, PaymentLog paymentLog,
                                  List<PaymentProductResponseDTO> paymentProductResponseDTOList, Long paymentDate) {
        this.paymentLogId = paymentLog.getId();
        this.paymentStatus = paymentLog.getPaymentStatus().getStatus();
        this.recipient = paymentLog.getRecipient();
        this.totalPrice = totalPrice;
        this.paymentDate = paymentDate;
        this.paymentProductResponseDTOList = paymentProductResponseDTOList;
        this.phoneNumber = paymentLog.getPhoneNumber();
        this.mainAddress = paymentLog.getMainAddress();
        this.addressDetail = paymentLog.getAddressDetail();
        this.postNumber = paymentLog.getPostNumber();
        this.deliveryMessage = paymentLog.getDeliveryMessage();
        this.trackingNumber = paymentLog.getTrackingNumber();
        this.usedPoint = paymentLog.getUsedPoint();
    }


}
