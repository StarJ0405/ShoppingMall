package com.team.shopping.Domains;

import com.team.shopping.Enums.Type;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser sender;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @Column(length = 50)
    private String message;

    private Type type;

    private LocalDateTime createDate;
}
