package com.team.shopping.Controllers;

import com.team.shopping.DTOs.ProductResponseDTO;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final MultiService multiService;

    @GetMapping
    public ResponseEntity<?> searchedProductList (@RequestHeader("page") int page,
                                                  @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                                  @RequestHeader("sort") int sort){
        try {
            Page<ProductResponseDTO> productResponseDTOList = this.multiService.searchByKeyword(page, keyword, sort);
            return ResponseEntity.status(HttpStatus.OK).body(productResponseDTOList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당하는 제품 찾을 수 없음");
        }
    }
}
