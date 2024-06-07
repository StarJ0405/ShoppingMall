package com.team.shopping.Controllers;

import com.team.shopping.DTOs.ProductRequestDTO;
import com.team.shopping.DTOs.ProductResponseDTO;
import com.team.shopping.DTOs.UserResponseDTO;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {
    private final MultiService multiService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestHeader("Authorization") String accessToken,
                                           @RequestBody ProductRequestDTO requestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            this.multiService.saveProduct(requestDTO, username);
            return tokenRecord.getResponseEntity("문제 없음");
        }
        return tokenRecord.getResponseEntity();
    }
}
