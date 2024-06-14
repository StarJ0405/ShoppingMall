package com.team.shopping.Controllers;

import com.team.shopping.DTOs.ReviewRequestDTO;
import com.team.shopping.DTOs.ReviewResponseDTO;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final MultiService multiService;

    @PostMapping
    public ResponseEntity<?> addReview (@RequestHeader("Authorization") String accessToken,
                                      @RequestBody ReviewRequestDTO reviewRequestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        try {
            if (tokenRecord.isOK()) {
                String username = tokenRecord.username();
                // 기능
                List<ReviewResponseDTO> reviewResponseDTOList = this.multiService.addToReview(username, reviewRequestDTO);
                return tokenRecord.getResponseEntity(reviewResponseDTOList);
            }
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("구매기록에 해당상품 없음");
        }
        return tokenRecord.getResponseEntity();
    }

    @GetMapping
    public ResponseEntity<?> reviewList (@RequestHeader("ProductId") Long productId) {
        List<ReviewResponseDTO> reviewResponseDTOList = this.multiService.getReviewList(productId);
        return ResponseEntity.status(HttpStatus.OK).body(reviewResponseDTOList);
    }

}
