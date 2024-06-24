package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.QWish;
import com.team.shopping.Domains.Wish;
import com.team.shopping.Repositories.Custom.WishListRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class WishListRepositoryImpl implements WishListRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QWish qWish = QWish.wish;


    @Override
    public Optional<Wish> findByProduct(Product product) {
        Wish wish = jpaQueryFactory.selectFrom(qWish).where(qWish.product.eq(product)).fetchOne();
        return Optional.ofNullable(wish);
    }
}
