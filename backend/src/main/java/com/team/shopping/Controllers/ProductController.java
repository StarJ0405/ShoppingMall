package com.team.shopping.Controllers;

import com.team.shopping.DTOs.ProductCreateRequestDTO;
import com.team.shopping.DTOs.ProductResponseDTO;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {
    private final MultiService multiService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestHeader("Authorization") String accessToken,
                                           @RequestBody ProductCreateRequestDTO requestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            this.multiService.saveProduct(requestDTO, username);
            return tokenRecord.getResponseEntity("문제 없음");
        }
        return tokenRecord.getResponseEntity();
    }

    @GetMapping
    public ResponseEntity<?> getProduct(@RequestHeader("ProductId") Long productId) {
        try {
            ProductResponseDTO productResponseDTO = multiService.getProduct(productId);
            return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/List")
    public ResponseEntity<?> getProductList() {
        try {
            List<ProductResponseDTO> productResponseDTOList = multiService.getProductList();
            return ResponseEntity.status(HttpStatus.OK).body(productResponseDTOList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
