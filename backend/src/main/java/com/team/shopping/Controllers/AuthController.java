package com.team.shopping.Controllers;


import com.team.shopping.DTOs.AuthRequestDTO;
import com.team.shopping.DTOs.AuthResponseDTO;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final MultiService multiService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO requestDto) {
        AuthResponseDTO responseDto = this.multiService.login(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("REFRESH_TOKEN") String refreshToken) {
        String newAccessToken = this.multiService.refreshToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(newAccessToken);
    }

}
