package com.team.shopping.Domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
public class Recent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private LocalDateTime createDate;
}
