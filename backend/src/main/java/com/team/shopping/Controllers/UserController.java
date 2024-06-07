package com.team.shopping.Controllers;

import com.team.shopping.DTOs.ProductRequestDTO;
import com.team.shopping.DTOs.ProductResponseDTO;
import com.team.shopping.DTOs.SignupRequestDTO;
import com.team.shopping.DTOs.UserResponseDTO;
import com.team.shopping.Exceptions.DataDuplicateException;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.Module.UserService;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final MultiService multiService;

    @PostMapping
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO signupRequestDTO) {
        try {
            multiService.signup(signupRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (DataDuplicateException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }
    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String accessToken,
                                        @RequestBody SignupRequestDTO signupRequestDTO) {
        try {
            TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
            if (tokenRecord.isOK()) {
                String username = tokenRecord.username();
                multiService.deleteUser(signupRequestDTO.getUsername(), username);
                return tokenRecord.getResponseEntity("문제 없음");
            }
            return tokenRecord.getResponseEntity();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }

    @GetMapping
    public ResponseEntity<?> profile (@RequestHeader("Authorization") String accessToken) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            // 기능
            UserResponseDTO userResponseDTO = this.multiService.getProfile(username);
            return tokenRecord.getResponseEntity(userResponseDTO);
        }
        return tokenRecord.getResponseEntity();
    }

    /**
     * wishList Function
     **/

    @GetMapping("/wishList")
    public ResponseEntity<?> wishList (@RequestHeader("Authorization") String accessToken) {
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
    public ResponseEntity<?> addWishList (@RequestHeader("Authorization") String accessToken,
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
    public ResponseEntity<?> deleteToWishList (@RequestHeader("Authorization") String accessToken,
                                               @RequestBody ProductRequestDTO productRequestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            // 기능
            List<ProductResponseDTO> wishListResponseDTO = this.multiService.deleteToWishList(username, productRequestDTO);
            return tokenRecord.getResponseEntity(wishListResponseDTO);
        }
        return tokenRecord.getResponseEntity();
    }
}