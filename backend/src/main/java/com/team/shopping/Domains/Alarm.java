package com.team.shopping.Domains;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser target;
    private String sender;
    private String message;
    private String url;

    private boolean isRead;
    @Setter(AccessLevel.NONE)
    private LocalDateTime createDate;

    @Builder
    public Alarm(SiteUser target, String message, String sender, boolean isRead, String url) {
        this.target = target;
        this.message = message;
        this.sender = sender;
        this.isRead = isRead;
        this.createDate = LocalDateTime.now();
        this.url = url;
    }
}
