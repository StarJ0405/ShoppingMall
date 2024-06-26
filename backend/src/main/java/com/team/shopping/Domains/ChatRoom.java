package com.team.shopping.Domains;

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

    @Builder
    public ChatRoom(SiteUser user1, SiteUser user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.createDate = LocalDateTime.now();
    }
}
