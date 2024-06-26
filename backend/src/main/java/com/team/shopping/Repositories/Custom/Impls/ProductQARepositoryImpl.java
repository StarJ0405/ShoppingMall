package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.ProductQA;
import com.team.shopping.Domains.QProductQA;
import com.team.shopping.Repositories.Custom.ProductQARepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProductQARepositoryImpl implements ProductQARepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QProductQA qProductQA = QProductQA.productQA;


    @Override
    public List<ProductQA> findByProduct(Product product) {
        return jpaQueryFactory.selectFrom(qProductQA).where(qProductQA.product.eq(product)).fetch();
    }
}
