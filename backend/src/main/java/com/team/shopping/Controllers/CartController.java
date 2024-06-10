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
    public ResponseEntity<?> cartList (@RequestHeader("Authorization") String accessToken,
                                       @RequestHeader("count") int count,
                                       @RequestBody CartRequestDTO cartRequestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            // 기능
            List<CartResponseDTO> cartResponseDTOList = this.multiService.addToCart(username, cartRequestDTO, count);
            return tokenRecord.getResponseEntity(cartResponseDTOList);
        }
        return tokenRecord.getResponseEntity();
    }
}