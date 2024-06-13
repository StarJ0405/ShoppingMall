package com.team.shopping.Enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    WAITING_DEPOSIT ("입금대기중"),
    PAYMENT_COMPLETED ("결제완료"),
    PREPARING_PRODUCT ("상품준비중"),
    PRODUCT_DISPATCHED ("상품발송"),
    IN_TRANSIT ("배송중"),
    DELIVERED ("배송완료"),
    WAITING_PURCHASE_CONFIRMATION ("구매확정대기중"),
    PURCHASE_CONFIRMED ("구매확정"),
    WAITING_CANCELLATION ("취소대기중"),
    CANCELLATION_CONFIRMED ("취소확정")
    //
    ;
    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }
}
