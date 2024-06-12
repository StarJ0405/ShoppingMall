package com.team.shopping.Controllers;

import com.team.shopping.DTOs.PaymentLogResponseDTO;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
