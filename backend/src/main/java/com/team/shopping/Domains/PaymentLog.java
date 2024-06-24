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

    private String recipient;

    private String phoneNumber;

    private String mainAddress;

    private String addressDetail;

    private int postNumber;

    private String deliveryMessage;

    private int trackingNumber;

    private Long usedPoint;

    @Column(columnDefinition = "TEXT")
    private String info;

    @Builder
    public PaymentLog (SiteUser user, String info, LocalDateTime createDate,
                       PaymentStatus paymentStatus, String recipient,
                       String phoneNumber, String mainAddress, String addressDetail,
                       int postNumber, String deliveryMessage, Long usedPoint) {
        this.order = user;
        this.info = info;
        this.createDate = createDate;
        this.paymentStatus = paymentStatus;
        this.recipient = recipient;
        this.phoneNumber = phoneNumber;
        this.mainAddress = mainAddress;
        this.addressDetail = addressDetail;
        this.postNumber = postNumber;
        this.deliveryMessage = deliveryMessage;
        this.usedPoint = usedPoint;
    }
}
