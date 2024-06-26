package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.QReview;
import com.team.shopping.Domains.Review;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.Custom.ReviewRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactor;
    QReview qReview = QReview.review;


    @Override
    public Optional<Review> findByProduct(Product product) {
        Review review = jpaQueryFactor.selectFrom(qReview).where(qReview.product.eq(product)).fetchOne();
        return Optional.ofNullable(review);
    }

    @Override
    public Optional<Review> findByPaymentProductId(Long paymentProductId) {
        Review review = jpaQueryFactor.selectFrom(qReview).where(qReview.paymentProductId.eq(paymentProductId)).fetchOne();
        return Optional.ofNullable(review);
    }

    @Override
    public List<Review> getMyReview(SiteUser user) {
        return jpaQueryFactor.selectFrom(qReview).where(qReview.author.eq(user)).fetch();
    }
}
