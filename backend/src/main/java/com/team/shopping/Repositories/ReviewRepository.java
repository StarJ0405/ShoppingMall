package com.team.shopping.Repositories;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductOrderByCreateDateDesc(Product product);
    void deleteByProduct(Product product);

}
