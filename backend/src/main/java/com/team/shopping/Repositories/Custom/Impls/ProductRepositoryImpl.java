package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.*;
import com.team.shopping.Enums.Sorts;
import com.team.shopping.Repositories.Custom.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    QProduct qProduct = QProduct.product;
    QReview qReview = QReview.review;
    QTag qTag = QTag.tag;
    QCategory qCategory = QCategory.category;


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

    @Override
    public Page<Product> searchByKeyword(Pageable pageable, String keyword, Sorts sorts) {
        JPAQuery<Product> query = jpaQueryFactory
                .selectFrom(qProduct).distinct()
                .join(qTag).on(qTag.product.id.eq(qProduct.id))
                .where(qProduct.title.contains(keyword).or(qTag.name.contains(keyword).or(qProduct.seller.nickname.eq(keyword))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // 정렬 조건 추가
        switch (sorts) {
            case CREATE_DATE_DESC:
                query.orderBy(qProduct.createDate.desc());
                break;
            case PRICE_DESC:
                query.orderBy(qProduct.price.desc());
                break;
            case PRICE_ASC:
                query.orderBy(qProduct.price.asc());
                break;
            case REVIEW_COUNT_DESC:
                query.leftJoin(qReview)
                        .on(qProduct.id.eq(qReview.product.id))
                        .groupBy(qProduct.id)
                        .orderBy(qReview.count().desc());
                break;
            default:
                throw new IllegalArgumentException("Invalid sort option: " + sorts);
        }

        QueryResults<Product> results = query.fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public Page<Product> findByTitleOrTagGroupByCategory(Pageable pageable, String keyword, Long categoryId, Sorts sorts) {
        JPAQuery<Product> query = jpaQueryFactory
                .selectFrom(qProduct).distinct()
                .join(qTag).on(qTag.product.id.eq(qProduct.id))
                .join(qCategory).on(qCategory.id.eq(qProduct.category.id))
                .where(qCategory.id.eq(categoryId).and(qProduct.title.contains(keyword).or(qTag.name.contains(keyword))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        switch (sorts) {
            case CREATE_DATE_DESC:
                query.orderBy(qProduct.createDate.desc());
                break;
            case PRICE_DESC:
                query.orderBy(qProduct.price.desc());
                break;
            case PRICE_ASC:
                query.orderBy(qProduct.price.asc());
                break;
            case REVIEW_COUNT_DESC:
                query.leftJoin(qReview)
                        .on(qProduct.id.eq(qReview.product.id))
                        .groupBy(qProduct.id)
                        .orderBy(qReview.count().desc());
                break;
            default:
                throw new IllegalArgumentException("Invalid sort option: " + sorts);
        }

        QueryResults<Product> results = query.fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

}

