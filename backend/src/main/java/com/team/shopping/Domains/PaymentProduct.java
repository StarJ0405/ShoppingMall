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
public class PaymentProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentLog paymentLog;

    private Long productId;

    private String seller;

    private int price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String title;

    @Column(length = 50)
    private String brand;

    private int count;

    @Builder
    public PaymentProduct (PaymentLog paymentLog, Long productId,
                           String seller, int price, String description,
                           String title, String brand, int count) {

        this.paymentLog = paymentLog;
        this.productId = productId;
        this.seller = seller;
        this.price = price;
        this.description = description;
        this.title = title;
        this.brand = brand;
        this.count = count;

    }

}
