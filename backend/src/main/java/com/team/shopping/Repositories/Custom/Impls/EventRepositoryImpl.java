package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.*;
import com.team.shopping.Repositories.Custom.EventRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QEvent qEvent = QEvent.event;
    QEventProduct qEventProduct = QEventProduct.eventProduct;
    QProduct qProduct = QProduct.product;


    @Override
    public List<Event> findByProduct(Product product) {
        return jpaQueryFactory.selectFrom(qEvent)
                .leftJoin(qEventProduct).on(qEvent.id.eq(qEventProduct.event.id))
                .leftJoin(qProduct).on(qEventProduct.product.id.eq(qProduct.id))
                .where(qProduct.eq(product))
                .fetch();
    }

    @Override
    public List<Event> findMyList(SiteUser user) {
        return jpaQueryFactory.selectFrom(qEvent).where(qEvent.creator.eq(user)).fetch();
    }
}
