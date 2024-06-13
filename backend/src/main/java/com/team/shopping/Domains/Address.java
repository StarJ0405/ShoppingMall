package com.team.shopping.Domains;

import jakarta.persistence.*;
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

    private String orderedName;

    private String phoneNumber;

    private String mainAddress;

    private String addressDetail;

    private int postNumber;

    private String deliveryMessage;
}
