package com.team.shopping.Services.Module;

import com.team.shopping.Domains.ChatMessage;
import com.team.shopping.Domains.ChatRoom;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Enums.ChatType;
import com.team.shopping.Repositories.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage save(ChatRoom chatRoom, SiteUser senderUser, String message, int type) {
        return chatMessageRepository.save(ChatMessage.builder() //
                .chatRoom(chatRoom) //
                .sender(senderUser) //
                .message(message) //
                .type(ChatType.values()[type]) //
                .build());
    }

    public List<ChatMessage> getChatList(ChatRoom chatRoom) {
        return chatMessageRepository.getChatList(chatRoom);
    }
}
