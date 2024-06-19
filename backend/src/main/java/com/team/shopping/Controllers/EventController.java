package com.team.shopping.Controllers;

import com.team.shopping.DTOs.EventRequestDTO;
import com.team.shopping.DTOs.EventResponseDTO;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/event")
public class EventController {

    private final MultiService multiService;

    @PostMapping
    public ResponseEntity<?> createEvent (@RequestHeader("Authorization") String accessToken,
                                          @RequestBody EventRequestDTO eventRequestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        try {
            if (tokenRecord.isOK()) {
                String username = tokenRecord.username();
                EventResponseDTO eventResponseDTO = this.multiService.createEvent(username, eventRequestDTO);
                return tokenRecord.getResponseEntity(eventResponseDTO);
            }
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("제품을 찾지 못했습니다.");
        }
        return tokenRecord.getResponseEntity();
    }
}
