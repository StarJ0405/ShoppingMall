package com.team.shopping.Domains;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class PaymentProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentProduct paymentProduct;

    private String optionListName;

    private String optionName;

    private int optionCount;

    private int optionPrice;

    @Builder
    public PaymentProductDetail (PaymentProduct paymentProduct, String optionName,
                                 int optionCount, int optionPrice, String optionListName) {
        this.optionListName = optionListName;
        this.paymentProduct = paymentProduct;
        this.optionName = optionName;
        this.optionCount = optionCount;
        this.optionPrice = optionPrice;
    }
}
