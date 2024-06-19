package com.team.shopping.DTOs;

import com.team.shopping.Domains.Address;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDTO {

    private Long addressId;

    private String title;

    private String recipient;

    private String phoneNumber;

    private String mainAddress;

    private String addressDetail;

    private int postNumber;

    private String deliveryMessage;

    @Builder
    public AddressResponseDTO (Address address) {
        this.addressId = address.getId();
        this.title = address.getTitle();
        this.recipient = address.getRecipient();
        this.phoneNumber = address.getPhoneNumber();
        this.mainAddress = address.getMainAddress();
        this.addressDetail = address.getAddressDetail();
        this.postNumber = address.getPostNumber();
        this.deliveryMessage = address.getDeliveryMessage();
    }
}
