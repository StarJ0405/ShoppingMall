package com.team.shopping.Controllers;

import com.team.shopping.DTOs.*;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {
    private final MultiService multiService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestHeader("Authorization") String accessToken, @RequestBody ProductCreateRequestDTO requestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            List<OptionListRequestDTO> listRequestDTOS = requestDTO.getOptionLists();
            if (listRequestDTOS != null) this.multiService.saveProduct(requestDTO, username);
            return tokenRecord.getResponseEntity("문제 없음");
        }
        return tokenRecord.getResponseEntity();
    }

    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestHeader("Authorization") String accessToken,
                                           @RequestBody ProductCreateRequestDTO requestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            ProductResponseDTO responseDTO = multiService.updateProduct(username, requestDTO);
            return tokenRecord.getResponseEntity(responseDTO);
        }
        return tokenRecord.getResponseEntity();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProduct(@RequestHeader("Authorization") String accessToken, @RequestHeader("ProductId") Long productId) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            this.multiService.deleteProduct(productId, username);
            return tokenRecord.getResponseEntity("문제 없음");
        }
        return tokenRecord.getResponseEntity();
    }

    @GetMapping
    public ResponseEntity<?> getProduct(@RequestHeader("ProductId") Long productId) {
        ProductResponseDTO productResponseDTO = multiService.getProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getProductList() {
        try {
            List<ProductResponseDTO> productResponseDTOList = multiService.getProductList();
            return ResponseEntity.status(HttpStatus.OK).body(productResponseDTOList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not productList");
        }
    }

    @GetMapping("/best")
    public ResponseEntity<?> bestProductList() {
        try {
            List<ProductResponseDTO> productResponseDTOList = multiService.getBestList();
            return ResponseEntity.status(HttpStatus.OK).body(productResponseDTOList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("베스트리스트 찾을 수 없음");
        }
    }

    @GetMapping("/latest")
    public ResponseEntity<?> latestProductList(@RequestHeader("Page") int page) {
        try {
            Page<ProductResponseDTO> productResponseDTOList = multiService.getLatestList(page);

            return ResponseEntity.status(HttpStatus.OK).body(productResponseDTOList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not desc list");
        }
    }

    @GetMapping("/question")
    public ResponseEntity<?> getProductQuestionAnswerList(@RequestHeader("ProductId") Long productId) {
        List<ProductQAResponseDTO> list = this.multiService.getQA(productId);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @PostMapping("/question")
    public ResponseEntity<?> productQuestion(@RequestHeader("Authorization") String
                                                     accessToken, @RequestBody ProductQARequestDTO requestDTO) {
        try {
            TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
            if (tokenRecord.isOK()) {
                String username = tokenRecord.username();
                this.multiService.productQASave(username, requestDTO);
                return ResponseEntity.status(HttpStatus.OK).body(this.multiService.getQA(requestDTO.getProductId()));
            }
            return tokenRecord.getResponseEntity();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/answer")
    public ResponseEntity<?> productAnswer(@RequestHeader("Authorization") String accessToken, @RequestBody ProductQARequestDTO requestDTO) {
        try {
            TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
            if (tokenRecord.isOK()) {
                String username = tokenRecord.username();
                this.multiService.productQAUpdate(username, requestDTO);
                return ResponseEntity.status(HttpStatus.OK).body(this.multiService.getQA(requestDTO.getProductId()));
            }
            return tokenRecord.getResponseEntity();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/myProducts")
    public ResponseEntity<?> myProducts(@RequestHeader("Authorization") String accessToken) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        try {
            if (tokenRecord.isOK()) {
                String username = tokenRecord.username();
                List<ProductResponseDTO> productResponseDTOList = this.multiService.getMyProductList(username);
                return tokenRecord.getResponseEntity(productResponseDTOList);
            }
            return tokenRecord.getResponseEntity();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한 없음");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("올린 상품 없음");
        }
    }
}
