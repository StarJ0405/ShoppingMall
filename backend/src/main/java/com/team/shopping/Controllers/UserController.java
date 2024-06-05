package com.team.shopping.Controllers;

import com.team.shopping.DTOs.ProductRequestDTO;
import com.team.shopping.DTOs.ProductResponseDTO;
import com.team.shopping.DTOs.SignupRequestDTO;
import com.team.shopping.Exceptions.DataDuplicateException;
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

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO signupRequestDTO) {
        try {
            multiService.signup(signupRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (DataDuplicateException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }

    @GetMapping("/wishList")
    public ResponseEntity<?> wishList () {
        String username = "2ndsprout";
        List<ProductResponseDTO> wishListResponseDTO = this.multiService.getWishList(username);
        return ResponseEntity.status(HttpStatus.OK).body(wishListResponseDTO);
    }

    @PostMapping("/wishList")
    public ResponseEntity<?> addWishList (@RequestBody ProductRequestDTO productRequestDTO) {
        String username = "2ndsprout";
        List<ProductResponseDTO> wishListResponseDTO = this.multiService.addToWishList(username, productRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(wishListResponseDTO);
    }
    @DeleteMapping("/wishList")
    public ResponseEntity<?> deleteToWishList (@RequestBody ProductRequestDTO productRequestDTO) {
        String username = "2ndsprout";
        List<ProductResponseDTO> wishListResponseDTO = this.multiService.deleteToWishList(username, productRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(wishListResponseDTO);
    }
}
