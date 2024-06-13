package com.team.shopping.Enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    AwaitingDeposit ("입금대기중"),
    PaymentCompleted ("결제완료"),
    PreparingProduct ("상품준비중"),
    ProductDispatched ("상품발송"),
    InTransit ("배송중"),
    Delivered ("배송완료"),
    AwaitingPurchaseConfirmation ("구매확정대기중"),
    PurchaseConfirmed ("구매확정"),
    AwaitingCancellation ("취소대기중"),
    CancellationConfirmed ("취소확정")
    //
    ;
    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }
}
