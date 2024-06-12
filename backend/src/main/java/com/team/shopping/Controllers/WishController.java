package com.team.shopping.Controllers;

import com.team.shopping.DTOs.ProductRequestDTO;
import com.team.shopping.DTOs.ProductResponseDTO;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class WishController {

    private final MultiService multiService;

    @GetMapping("/wishList")
    public ResponseEntity<?> wishList(@RequestHeader("Authorization") String accessToken) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            // 기능
            List<ProductResponseDTO> wishListResponseDTO = this.multiService.getWishList(username);
            return tokenRecord.getResponseEntity(wishListResponseDTO);
        }
        return tokenRecord.getResponseEntity();
    }

    @PostMapping("/wishList")
    public ResponseEntity<?> addWishList(@RequestHeader("Authorization") String accessToken,
                                         @RequestBody ProductRequestDTO productRequestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            // 기능
            List<ProductResponseDTO> wishListResponseDTO = this.multiService.addToWishList(username, productRequestDTO);
            return tokenRecord.getResponseEntity(wishListResponseDTO);
        }
        return tokenRecord.getResponseEntity();
    }

    @DeleteMapping("/wishList")
    public ResponseEntity<?> deleteToWishList(@RequestHeader("Authorization") String accessToken,
                                              @RequestHeader("productId") Long productId) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            // 기능
            List<ProductResponseDTO> wishListResponseDTO = this.multiService.deleteToWishList(username, productId);
            return tokenRecord.getResponseEntity(wishListResponseDTO);
        }
        return tokenRecord.getResponseEntity();
    }

    @DeleteMapping("/wishList/multi")
    public ResponseEntity<?> deleteMultipleToWishList (@RequestHeader("Authorization") String accessToken,
                                                       @RequestHeader("productIdList") List<Long> productIdList) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            // 기능
            List<ProductResponseDTO> wishListResponseDTO = this.multiService.deleteMultipleToWishList(username, productIdList);
            return tokenRecord.getResponseEntity(wishListResponseDTO);
        }
        return tokenRecord.getResponseEntity();
    }
}
