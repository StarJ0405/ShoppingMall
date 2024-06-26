package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.ChatMessage;
import com.team.shopping.Domains.ChatRoom;

import java.util.List;

public interface ChatMessageRepositoryCustom {
    List<ChatMessage> getChatList(ChatRoom chatRoom);
}
