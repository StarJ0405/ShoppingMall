package com.team.shopping.Services.Module;

import com.team.shopping.Domains.PaymentLog;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Enums.PaymentStatus;
import com.team.shopping.Repositories.PaymentLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentLogService {

    private PaymentLogRepository paymentLogRepository;

    public PaymentLog save (SiteUser user) {
        return this.paymentLogRepository.save(PaymentLog.builder()
                .user(user)
                .info("Payment info for user " + user.getUsername())
                .createDate(LocalDateTime.now())
                .paymentStatus(PaymentStatus.입금대기중)
                .build());
    }

    public List<PaymentLog> get (SiteUser order) {
        return this.paymentLogRepository.findByOrder(order);
    }
}
