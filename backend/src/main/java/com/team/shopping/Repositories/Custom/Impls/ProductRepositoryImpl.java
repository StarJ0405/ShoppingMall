package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.QProduct;
import com.team.shopping.Repositories.Custom.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    QProduct qProduct = QProduct.product;

    @Override
    public Page<Product> findAllPage(Pageable pageable) {
        QueryResults<Product> results = jpaQueryFactory
                .selectFrom(qProduct)
                .orderBy(qProduct.createDate.desc())
                .offset(pageable.getOffset()) // 페이지 시작 인덱스
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
