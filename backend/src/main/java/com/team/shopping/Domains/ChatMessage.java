package com.team.shopping.Domains;

import com.team.shopping.Enums.ChatType;
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
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser sender;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @Column(length = 200)
    private String message;

    private ChatType type;

    private LocalDateTime createDate;

    @Builder
    public ChatMessage(SiteUser sender, ChatRoom chatRoom, String message, ChatType type) {
        this.sender = sender;
        this.chatRoom = chatRoom;
        this.message = message;
        this.type = type;
        this.createDate = LocalDateTime.now();
    }
}
