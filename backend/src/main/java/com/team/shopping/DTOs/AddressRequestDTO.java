package com.team.shopping.DTOs;

import lombok.Getter;

@Getter
public class AddressRequestDTO {

    private Long addressId;

    private String title;

    private String recipient;

    private String phoneNumber;

    private String mainAddress;

    private String addressDetail;

    private int postNumber;

    private String deliveryMessage;

}
