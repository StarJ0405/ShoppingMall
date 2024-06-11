package com.team.shopping.Domains;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser seller;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    private int price;

    @Column(length = 300)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String detail;

    private LocalDateTime dateLimit;

    private int remain;

    @Column(length = 100)
    private String title;

    @Column(length = 100)
    private String delivery;

    @Column(length = 100)
    private String address;

    @Column(columnDefinition = "TEXT")
    private String receipt;

    @Column(length = 100)
    private String a_s;

    @Column(length = 50)
    private String brand;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @Builder
    public Product(SiteUser seller, Category category,
                   int price, String description, String detail,
                   LocalDateTime dateLimit, int remain, String title,
                   String delivery, String address, String receipt,
                   String a_s, String brand) {
        this.seller = seller;
        this.category = category;
        this.price = price;
        this.description = description;
        this.detail = detail;
        this.dateLimit = dateLimit;
        this.remain = remain;
        this.title = title;
        this.delivery = delivery;
        this.address = address;
        this.receipt = receipt;
        this.a_s = a_s;
        this.brand = brand;
        this.createDate = LocalDateTime.now();
    }
}
