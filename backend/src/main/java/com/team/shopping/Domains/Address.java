package com.team.shopping.Domains;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private SiteUser user;

    private String title;

    private String recipient;

    private String phoneNumber;

    private String mainAddress;

    private String addressDetail;

    private int postNumber;

    private String deliveryMessage;

    @Builder
    public Address (SiteUser user, String title, String recipient,
                    String phoneNumber, String mainAddress, String addressDetail,
                    int postNumber, String deliveryMessage) {
        this.user = user;
        this.title = title;
        this.recipient = recipient;
        this.phoneNumber = phoneNumber;
        this.mainAddress = mainAddress;
        this.addressDetail = addressDetail;
        this.postNumber = postNumber;
        this.deliveryMessage = deliveryMessage;
    }
}
