package com.StarJ.Social.Controllers;

import com.StarJ.Social.DTOs.AuthRequestDTO;
import com.StarJ.Social.DTOs.AuthResponseDTO;
import com.StarJ.Social.Service.Modules.AuthService;
import com.StarJ.Social.Service.MultiService;
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
