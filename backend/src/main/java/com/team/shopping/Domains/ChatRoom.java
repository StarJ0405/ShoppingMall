package com.team.shopping.Domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser user1;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser user2;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;


}
