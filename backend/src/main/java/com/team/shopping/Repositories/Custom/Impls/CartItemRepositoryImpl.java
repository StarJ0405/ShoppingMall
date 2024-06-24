package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.CartItem;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.QCartItem;
import com.team.shopping.Repositories.Custom.CartItemRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CartItemRepositoryImpl implements CartItemRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    QCartItem qCartItem = QCartItem.cartItem;

    @Override
    public Optional<CartItem> findByProduct(Product product) {
        CartItem cartItem = jpaQueryFactory.selectFrom(qCartItem).where(qCartItem.product.eq(product)).fetchOne();
        return Optional.ofNullable(cartItem);
    }
}
