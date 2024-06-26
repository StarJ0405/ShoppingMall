package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.Review;
import com.team.shopping.Domains.SiteUser;

import java.util.List;
import java.util.Optional;

public interface ReviewRepositoryCustom {
    Optional<Review> findByProduct(Product product);
    Optional<Review> findByPaymentProductId(Long paymentProductId);

    List<Review> getMyReview(SiteUser user);
}
