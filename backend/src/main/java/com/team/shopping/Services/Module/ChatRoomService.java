package com.team.shopping.Services.Module;

import com.team.shopping.Domains.ChatRoom;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public Optional<ChatRoom> getChatRoom(SiteUser username, SiteUser targetName) {
        return chatRoomRepository.getChatRoom(username, targetName);
    }

    public ChatRoom save(SiteUser sendUser, SiteUser acceptUser) {
        return chatRoomRepository.save(ChatRoom.builder()
                .user1(sendUser)
                .user2(acceptUser)
                .build());
    }


    public ChatRoom get(Long roomId) {
        return chatRoomRepository.getReferenceById(roomId);
    }
}
