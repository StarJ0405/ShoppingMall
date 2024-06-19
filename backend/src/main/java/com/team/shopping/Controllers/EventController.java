package com.team.shopping.Controllers;

import com.team.shopping.DTOs.EventRequestDTO;
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

                return tokenRecord.getResponseEntity();
            }
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
        }catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }
        return tokenRecord.getResponseEntity();
    }
}
