package com.team.shopping.Services.Module;

import com.team.shopping.Domains.ChatMessage;
import com.team.shopping.Domains.ChatRoom;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Enums.Type;
import com.team.shopping.Repositories.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    public void save(ChatRoom chatRoom, SiteUser senderUser, String message, int type) {
        chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(senderUser)
                .message(message)
                .type(Type.values()[type]).build());
    }

    public List<ChatMessage> getChatList(ChatRoom chatRoom) {
        return chatMessageRepository.getChatList(chatRoom);
    }
}
