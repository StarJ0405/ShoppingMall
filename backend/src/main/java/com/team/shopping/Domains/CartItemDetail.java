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
public class CartItemDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CartItem cartItem;

    @ManyToOne(fetch = FetchType.LAZY)
    private Options options;

    @Builder
    public CartItemDetail (CartItem cartItem, Options options) {
        this.cartItem = cartItem;
        this.options = options;
    }


}
