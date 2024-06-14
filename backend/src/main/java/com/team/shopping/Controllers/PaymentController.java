package com.team.shopping.Controllers;

import com.team.shopping.DTOs.PaymentLogRequestDTO;
import com.team.shopping.DTOs.PaymentLogResponseDTO;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final MultiService multiService;

    @GetMapping("/logList")
    public ResponseEntity<?> logList (@RequestHeader("Authorization") String accessToken) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            // 기능
            List<PaymentLogResponseDTO> paymentLogResponseDTOList = this.multiService.getPaymentLogList(username);
            return tokenRecord.getResponseEntity(paymentLogResponseDTOList);
        }
        return tokenRecord.getResponseEntity();
    }

    @PostMapping("/logList")
    public ResponseEntity<?> logList (@RequestHeader("Authorization") String accessToken,
                                      @RequestBody PaymentLogRequestDTO paymentLogRequestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            PaymentLogResponseDTO paymentLogResponseDTO = this.multiService.addPaymentLog(username, paymentLogRequestDTO);
            return tokenRecord.getResponseEntity(paymentLogResponseDTO);
        }
        return tokenRecord.getResponseEntity();
    }
}
