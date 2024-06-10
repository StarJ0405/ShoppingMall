package com.team.shopping.Controllers;

import com.team.shopping.DTOs.ImageRequestDTO;
import com.team.shopping.DTOs.ImageResponseDTO;
import com.team.shopping.DTOs.ProductCreateRequestDTO;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {
    private final MultiService multiService;

    @PostMapping
    public ResponseEntity<?> tempImage(@RequestHeader("Authorization") String accessToken,
                                       @RequestBody ImageRequestDTO imageRequestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            ImageResponseDTO imageResponseDTO = this.multiService.tempUpload(imageRequestDTO, username);
            return tokenRecord.getResponseEntity(imageResponseDTO);
        }
        return tokenRecord.getResponseEntity();
    }
}
