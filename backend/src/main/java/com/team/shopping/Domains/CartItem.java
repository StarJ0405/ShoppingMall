package com.team.shopping.Domains;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private int count;

    private LocalDateTime createDate;

    @Builder
    public CartItem (SiteUser user, Product product, int count, LocalDateTime createDate) {
        this.user = user;
        this.product = product;
        this.count = count;
        this.createDate = createDate;
    }

    public void updateCount(int count) {
        this.count = count;
    }
}
