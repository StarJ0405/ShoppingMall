package com.team.shopping.Services.Module;

import com.team.shopping.DTOs.ReviewRequestDTO;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.Review;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review save (SiteUser author, ReviewRequestDTO reviewRequestDTO, Product product) {
        return this.reviewRepository.save(Review.builder()
                .author(author)
                .grade(reviewRequestDTO.getGrade())
                .product(product)
                .title(reviewRequestDTO.getTitle())
                .content(reviewRequestDTO.getContent())
                .build());
    }

    public List<Review> getList (Product product) {
        return this.reviewRepository.findByProductOrderByCreateDateDesc(product);
    }


}
