package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.EventProduct;
import com.team.shopping.Domains.Options;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.QEventProduct;
import com.team.shopping.Repositories.Custom.EventProductRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class EventProductRepositoryImpl implements EventProductRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QEventProduct  qEventProduct = QEventProduct.eventProduct;

    @Override
    public List<EventProduct> findByProduct(Product product) {
        return jpaQueryFactory.selectFrom(qEventProduct).where(qEventProduct.product.eq(product)).fetch();
    }
}
