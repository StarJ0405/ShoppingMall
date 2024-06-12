package com.team.shopping.Domains;

import com.team.shopping.Enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class PaymentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser order;

    private LocalDateTime createDate;

    private PaymentStatus paymentStatus;

    @Column(columnDefinition = "TEXT")
    private String info;

    @Builder
    public PaymentLog (SiteUser user, String info, LocalDateTime createDate, PaymentStatus paymentStatus) {
        this.order = user;
        this.info = info;
        this.createDate = createDate;
        this.paymentStatus = paymentStatus;
    }
}
