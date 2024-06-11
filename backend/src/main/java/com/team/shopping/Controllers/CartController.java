package com.team.shopping.Controllers;

import com.team.shopping.DTOs.CartRequestDTO;
import com.team.shopping.DTOs.CartResponseDTO;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final MultiService multiService;

    @GetMapping("/cartList")
    public ResponseEntity<?> cartList (@RequestHeader("Authorization") String accessToken) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            // 기능
            List<CartResponseDTO> cartResponseDTOList = this.multiService.getCart(username);
            return tokenRecord.getResponseEntity(cartResponseDTOList);
        }
        return tokenRecord.getResponseEntity();
    }

    @PostMapping("/cartList")
    public ResponseEntity<?> addToCartList (@RequestHeader("Authorization") String accessToken,
                                            @RequestBody CartRequestDTO cartRequestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            // 기능
            List<CartResponseDTO> cartResponseDTOList = this.multiService.addToCart(username, cartRequestDTO);
            return tokenRecord.getResponseEntity(cartResponseDTOList);
        }
        return tokenRecord.getResponseEntity();
    }

    @PutMapping("/cartList")
    public ResponseEntity<?> updateToCart (@RequestHeader("Authorization") String accessToken,
                                           @RequestBody CartRequestDTO cartRequestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            // 기능
            List<CartResponseDTO> cartResponseDTOList = this.multiService.updateToCart(username, cartRequestDTO);
            return tokenRecord.getResponseEntity(cartResponseDTOList);
        }
        return tokenRecord.getResponseEntity();

    }


    @DeleteMapping("/cartList")
    public ResponseEntity<?> deleteToCartList (@RequestHeader("Authorization") String accessToken,
                                               @RequestHeader("productId") Long productId) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            // 기능
            List<CartResponseDTO> cartResponseDTOList = this.multiService.deleteToCart(username, productId);
            return tokenRecord.getResponseEntity(cartResponseDTOList);
        }
        return tokenRecord.getResponseEntity();
    }

    @DeleteMapping("/cartList/multi")
    public ResponseEntity<?> deleteMultiToCartList (@RequestHeader("Authorization") String accessToken,
                                                    @RequestHeader("productIdList") List<Long> productIdList) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            // 기능
            List<CartResponseDTO> cartResponseDTOList = this.multiService.deleteMultipleToCart(username, productIdList);          return tokenRecord.getResponseEntity(cartResponseDTOList);
        }
        return tokenRecord.getResponseEntity();
    }

}
