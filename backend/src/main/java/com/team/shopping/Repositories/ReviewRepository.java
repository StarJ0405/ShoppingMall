package com.team.shopping.Repositories;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.Review;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.Custom.ReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    List<Review> findByProductOrderByCreateDateDesc(Product product);
    List<Review> findByAuthorAndProduct(SiteUser user, Product product);
}
