package com.team.shopping.Enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    입금대기중, 결제완료, 상품준비중, 상품발송,
    배송중, 배송완료, 구매확정대기중, 구매확정,
    취소대기중, 취소확정;
}
