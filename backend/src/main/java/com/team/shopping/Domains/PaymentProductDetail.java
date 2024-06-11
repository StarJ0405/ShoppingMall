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

    @ManyToOne
    private Options option;

    @Builder
    public PaymentProductDetail (PaymentProduct paymentProduct, Options option) {
        this.paymentProduct = paymentProduct;
        this.option = option;
    }
}
