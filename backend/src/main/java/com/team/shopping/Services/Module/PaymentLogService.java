package com.team.shopping.Services.Module;

import com.team.shopping.DTOs.PaymentLogRequestDTO;
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

    private final PaymentLogRepository paymentLogRepository;

    public PaymentLog save (SiteUser user, PaymentLogRequestDTO paymentLogRequestDTO) {
        return this.paymentLogRepository.save(PaymentLog.builder()
                .user(user)
                .info("Payment info for user " + user.getUsername())
                .createDate(LocalDateTime.now())
                .paymentStatus(PaymentStatus.WAITING_DEPOSIT)
                .recipient(paymentLogRequestDTO.getRecipient())
                .phoneNumber(paymentLogRequestDTO.getPhoneNumber())
                .mainAddress(paymentLogRequestDTO.getMainAddress())
                .addressDetail(paymentLogRequestDTO.getAddressDetail())
                .postNumber(paymentLogRequestDTO.getPostNumber())
                .deliveryMessage(paymentLogRequestDTO.getDeliveryMessage())
                .build());
    }

    public List<PaymentLog> get (SiteUser order) {
        return this.paymentLogRepository.findByOrderOrderByCreateDateDesc(order);
    }

    public void delete (PaymentLog paymentLog) {
        this.paymentLogRepository.delete(paymentLog);
    }
}
