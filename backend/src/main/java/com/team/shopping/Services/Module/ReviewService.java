package com.team.shopping.Services.Module;

import com.team.shopping.DTOs.ReviewRequestDTO;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.Review;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Review get (Long reviewId) {
        return this.reviewRepository.findById(reviewId).orElseThrow();
    }


    public void delete(Review review) {
        this.reviewRepository.delete(review);
    }

    public Review update (Review review, ReviewRequestDTO reviewRequestDTO) {
        review.setGrade(reviewRequestDTO.getGrade());
        review.setTitle(reviewRequestDTO.getTitle());
        review.setContent(reviewRequestDTO.getContent());
        review.setModifyDate(LocalDateTime.now());

        return this.reviewRepository.save(review);
    }
}
