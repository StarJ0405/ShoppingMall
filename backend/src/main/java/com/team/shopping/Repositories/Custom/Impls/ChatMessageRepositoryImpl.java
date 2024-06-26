package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.ChatMessage;
import com.team.shopping.Domains.ChatRoom;
import com.team.shopping.Domains.QChatMessage;
import com.team.shopping.Repositories.Custom.ChatMessageRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QChatMessage qChatMessage = QChatMessage.chatMessage;


    @Override
    public List<ChatMessage> getChatList(ChatRoom chatRoom) {
        return jpaQueryFactory.selectFrom(qChatMessage).where(qChatMessage.chatRoom.eq(chatRoom)).fetch();
    }
}
