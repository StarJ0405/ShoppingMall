package com.team.shopping.Services.Module;

import com.team.shopping.Domains.PaymentLog;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.PaymentLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentLogService {

    private PaymentLogRepository paymentLogRepository;

    public PaymentLog save (SiteUser user) {
        PaymentLog paymentLog = PaymentLog.builder()
                .user(user)
                .info("Payment info for user " + user.getUsername())
                .build();
        return this.paymentLogRepository.save(paymentLog);
    }
}
