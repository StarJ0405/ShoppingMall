package com.team.shopping.Controllers;

import com.team.shopping.DTOs.*;
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
            List<OptionListRequestDTO> listRequestDTOS = requestDTO.getOptionLists();
            if (listRequestDTOS != null)
                for (OptionListRequestDTO listRequestDTO : listRequestDTOS)
                    if (listRequestDTO.getChild() != null)
                        for (OptionRequestDTO optionRequestDTO : listRequestDTO.getChild())
                            System.out.println(optionRequestDTO.getName() + "," + optionRequestDTO.getCount() + ", " + optionRequestDTO.getPrice());
                    else System.out.println("child is null");
            else System.out.println("list is null");

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

    @GetMapping("/list")
    public ResponseEntity<?> getProductList() {
        try {
            List<ProductResponseDTO> productResponseDTOList = multiService.getProductList();
            return ResponseEntity.status(HttpStatus.OK).body(productResponseDTOList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/question")
    public ResponseEntity<?> productQuestion(@RequestHeader("Authorization") String accessToken, @RequestBody ProductQARequestDTO requestDTO) {
        try {
            TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
            if (tokenRecord.isOK()) {
                String username = tokenRecord.username();
                this.multiService.productQASave(username, requestDTO);
                return tokenRecord.getResponseEntity("문제 없음");
            }
            return tokenRecord.getResponseEntity();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/answer")
    public ResponseEntity<?> productAnswer(@RequestHeader("Authorization") String accessToken, @RequestBody ProductQARequestDTO requestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        try {
            if (tokenRecord.isOK()) {
                String username = tokenRecord.username();
                this.multiService.productQAUpdate(username, requestDTO);
                return tokenRecord.getResponseEntity("문제 없음");
            }
            return tokenRecord.getResponseEntity();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
