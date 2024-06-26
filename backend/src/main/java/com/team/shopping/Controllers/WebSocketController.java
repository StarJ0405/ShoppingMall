package com.team.shopping.Controllers;

import com.team.shopping.DTOs.ChatMessageRequestDTO;
import com.team.shopping.DTOs.ChatMessageResponseDTO;
import com.team.shopping.DTOs.ChatRoomResponseDTO;
import com.team.shopping.Domains.ChatMessage;
import com.team.shopping.Domains.ChatRoom;
import com.team.shopping.Records.TokenRecord;
import com.team.shopping.Services.MultiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebSocketController {
    private final MultiService multiService;

    @GetMapping
    public ResponseEntity<?> ChatRoom(@RequestHeader("Authorization") String accessToken, @RequestHeader("Username") String targetName) {
        TokenRecord tokenRecord = this.multiService.checkToken(accessToken);
        if (tokenRecord.isOK()) {
            String username = tokenRecord.username();
            ChatRoomResponseDTO chatRoomResponseDTO = multiService.ChatRoom(username, targetName);
            return tokenRecord.getResponseEntity(chatRoomResponseDTO);
        }
        return tokenRecord.getResponseEntity();
    }

    @MessageMapping("/chat")
    @SendTo("/sub/chat/{id}")
    public ChatRoomResponseDTO ChatMessage(@PathVariable("RoomId") Long roomId, ChatMessageRequestDTO requestDTO) {
        return multiService.saveChatMessage(roomId, requestDTO.getMessage(), requestDTO.getSender(), requestDTO.getType());
    }
}
