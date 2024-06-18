package com.team.shopping.Controllers;

import com.team.shopping.DTOs.AuthResponseDTO;
import com.team.shopping.DTOs.ProductResponseDTO;
import com.team.shopping.DTOs.RecentRequestDTO;
import com.team.shopping.DTOs.RecentResponseDTO;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recent")
public class RecentController {
    private final MultiService multiService;

    @PostMapping
    public ResponseEntity<?> createRecent(@RequestHeader("Authorization") String accessToken,
                                          @RequestBody RecentRequestDTO requestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            this.multiService.saveRecent(requestDTO.getProductId(), username);
            return tokenRecord.getResponseEntity("문제 없음");
        }
        return tokenRecord.getResponseEntity();
    }

    @GetMapping
    public ResponseEntity<?> getRecentList(@RequestHeader("Authorization") String accessToken){
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            List<RecentResponseDTO> responseDTOList = this.multiService.getRecentList(username);
            return tokenRecord.getResponseEntity(responseDTOList);
        }
        return tokenRecord.getResponseEntity();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteRecent(@RequestHeader("Authorization") String accessToken,
                                          @RequestHeader("RecentId") Long recentId) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            this.multiService.deleteRecent(recentId, username);
            return tokenRecord.getResponseEntity("recent delete");
        }
        return tokenRecord.getResponseEntity();
    }
}
