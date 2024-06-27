package com.team.shopping.Repositories.Custom.Impls;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.shopping.Domains.ChatRoom;
import com.team.shopping.Domains.QChatRoom;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.Custom.ChatRoomRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QChatRoom qChatRoom = QChatRoom.chatRoom;

    @Override
    public Optional<ChatRoom> getChatRoom(SiteUser sendUser, SiteUser acceptUser) {
        ChatRoom chatRoom = jpaQueryFactory.selectFrom(qChatRoom).where(qChatRoom.user1.eq(sendUser).and(qChatRoom.user2.eq(acceptUser)).or(qChatRoom.user1.eq(acceptUser).and(qChatRoom.user2.eq(sendUser)))).fetchOne();
        return Optional.ofNullable(chatRoom);
    }

    @Override
    public List<ChatRoom> getChatRoomList(String username) {
        return jpaQueryFactory.selectFrom(qChatRoom).where(qChatRoom.user1.username.eq(username).or(qChatRoom.user2.username.eq(username))).orderBy(qChatRoom.modifyDate.desc()).fetch();
    }
}
