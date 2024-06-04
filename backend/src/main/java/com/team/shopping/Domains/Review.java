package com.team.shopping.Domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(length = 50)
    private String title;

    @Column(length = 200)
    private String content;

    private Double grade;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;
}
