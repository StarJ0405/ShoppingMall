package com.team.shopping.Repositories;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductOrderByCreateDateDesc(Product product);

}
