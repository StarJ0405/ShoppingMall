package com.team.shopping.Controllers;

import com.team.shopping.DTOs.PaymentLogRequestDTO;
import com.team.shopping.DTOs.PaymentLogResponseDTO;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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
            List<PaymentLogResponseDTO> paymentLogResponseDTOList = this.multiService.getPaymentLogList(username);
            return tokenRecord.getResponseEntity(paymentLogResponseDTOList);
        }
        return tokenRecord.getResponseEntity();
    }

    @PostMapping("/logList")
    public ResponseEntity<?> addLogList(@RequestHeader("Authorization") String accessToken,
                                        @RequestBody PaymentLogRequestDTO paymentLogRequestDTO) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        try {
            if (tokenRecord.isOK()) {
                String username = tokenRecord.username();
                PaymentLogResponseDTO paymentLogResponseDTO = this.multiService.addPaymentLog(username, paymentLogRequestDTO);
                return tokenRecord.getResponseEntity(paymentLogResponseDTO);
            }
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("남은재고 부족");
        }catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("장바구니 비었음");
        }
        return tokenRecord.getResponseEntity();
    }
}
