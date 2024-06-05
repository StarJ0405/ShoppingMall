package com.team.shopping.Domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    private int price;

    @Column(length = 200)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String detail;

    private LocalDateTime dateLimit;

    private int count;

    @Column(length = 50)
    private String title;

    @Column(length = 50)
    private String delivery;

    @Column(length = 50)
    private String address;

    @Column(length = 50)
    private String receipt;

    @Column(length = 50)
    private String a_s;

    @Column(length = 50)
    private String brand;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "product")
    private List<ProductTag> productTagList = new ArrayList<>();
}
