package com.team.shopping.Controllers;

import com.team.shopping.DTOs.ChatMessageRequestDTO;
import com.team.shopping.DTOs.ChatMessageResponseDTO;
import com.team.shopping.DTOs.ChatRoomResponseDTO;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final MultiService multiService;

    @GetMapping
    public ResponseEntity<?> ChatRoom(@RequestHeader("Authorization") String accessToken, @RequestHeader("Username") String targetName) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            ChatRoomResponseDTO chatRoomResponseDTO = multiService.saveChatRoom(username, targetName);
            return tokenRecord.getResponseEntity(chatRoomResponseDTO);
        }
        return tokenRecord.getResponseEntity();
    }
    @GetMapping("/chats")
    public ResponseEntity<?> getChatList(@RequestHeader("Authorization") String accessToken, @RequestHeader("roomId") Long roomId){
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            List<ChatMessageResponseDTO> chatList = multiService.getChatList(username,roomId);
            return tokenRecord.getResponseEntity(chatList);
        }
        return tokenRecord.getResponseEntity();
    }
    @GetMapping("/rooms")
    public ResponseEntity<?> getChatRooms(@RequestHeader("Authorization") String accessToken){
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            List<ChatRoomResponseDTO> chatList = multiService.getChatRooms(username);
            return tokenRecord.getResponseEntity(chatList);
        }
        return tokenRecord.getResponseEntity();
    }
    @MessageMapping("/chat/{id}")
    @SendTo("/api/sub/chat/{id}")
    public ChatMessageResponseDTO ChatMessage(@DestinationVariable("id") Long roomId, ChatMessageRequestDTO requestDTO) {
        return multiService.saveChatMessage(roomId, requestDTO.getMessage(), requestDTO.getSender(), requestDTO.getType());
    }
}
