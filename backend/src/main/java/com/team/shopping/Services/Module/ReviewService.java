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
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review save(SiteUser author, ReviewRequestDTO reviewRequestDTO, Product product) {
        Double grade = validateGrade(reviewRequestDTO.getGrade());
        return this.reviewRepository.save(Review.builder()
                .author(author)
                .grade(grade)
                .product(product)
                .title(reviewRequestDTO.getTitle())
                .content(reviewRequestDTO.getContent())
                .paymentProductId(reviewRequestDTO.getPaymentProductId())
                .build());
    }

    private Double validateGrade(Double grade) {
        if (grade == null || grade < 0.0 || grade > 5.0 || (grade * 10) % 5 != 0) {
            throw new IllegalArgumentException("Grade must be in range [0.0, 5.0] and in increments of 0.5");
        }
        return grade;
    }

    public List<Review> getList (Product product) {
        return this.reviewRepository.findByProductOrderByCreateDateDesc(product);
    }

    public List<Review> getMyReviewByProduct (SiteUser user, Product product) {
        return this.reviewRepository.findByAuthorAndProduct(user, product);
    }

    public Review get (Long reviewId) {
        return this.reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("not found review"));
    }


    public void delete(Review review) {
        this.reviewRepository.delete(review);
    }

    public Review update (Review review, ReviewRequestDTO reviewRequestDTO) {
        Double grade = validateGrade(reviewRequestDTO.getGrade());
        review.setGrade(grade);
        review.setTitle(reviewRequestDTO.getTitle());
        review.setContent(reviewRequestDTO.getContent());
        review.setModifyDate(LocalDateTime.now());

        return this.reviewRepository.save(review);
    }

    public List<Review> getAll () {
        return this.reviewRepository.findAll();
    }

    public Optional<Review> findByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    public void updateContent(Review reviewKey, String detail) {
        reviewKey.setContent(detail);
        this.reviewRepository.save(reviewKey);
    }

    public Review findByPaymentProductId (Long id) {
        return this.reviewRepository.findByPaymentProductId(id)
                .orElseThrow(()->new NoSuchElementException("not found review"));
    }
}
