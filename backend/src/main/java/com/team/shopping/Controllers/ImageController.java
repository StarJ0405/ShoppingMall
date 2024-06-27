package com.team.shopping.Controllers;

import com.team.shopping.DTOs.ImageRequestDTO;
import com.team.shopping.DTOs.ImageResponseDTO;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {
    private final MultiService multiService;

    @PostMapping
    public ResponseEntity<?> saveTempImage(@RequestHeader("Authorization") String accessToken, ImageRequestDTO imageRequestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            ImageResponseDTO imageResponseDTO = this.multiService.tempUpload(imageRequestDTO, username);
            return tokenRecord.getResponseEntity(imageResponseDTO);
        }
        return tokenRecord.getResponseEntity();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTempImage(@RequestHeader("Authorization") String accessToken) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {

            String username = tokenRecord.username();
            this.multiService.deleteTempImage(username);
            return tokenRecord.getResponseEntity(true);
        }
        return tokenRecord.getResponseEntity();
    }

    @PostMapping("/list")
    public ResponseEntity<?> tempImageList(@RequestHeader("Authorization") String accessToken, ImageRequestDTO imageRequestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            ImageResponseDTO imageResponseDTO = this.multiService.tempImageList(imageRequestDTO, username);
            return tokenRecord.getResponseEntity(imageResponseDTO);
        }
        return tokenRecord.getResponseEntity();
    }

    @DeleteMapping("/list")
    public ResponseEntity<?> deleteTempImageList(@RequestHeader("Authorization") String accessToken) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            this.multiService.deleteTempImageList(username);
            return tokenRecord.getResponseEntity(true);
        }
        return tokenRecord.getResponseEntity();
    }

    @PostMapping("/chat")
    public ResponseEntity<?> postChatImage(@RequestHeader("Authorization") String accessToken, ImageRequestDTO imageRequestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            String url = this.multiService.saveChatImageMessage(username,imageRequestDTO.getFile(),imageRequestDTO.getRoomId());
            return tokenRecord.getResponseEntity(url);
        }
        return tokenRecord.getResponseEntity();
    }

}
