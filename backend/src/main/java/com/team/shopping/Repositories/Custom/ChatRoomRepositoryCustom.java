package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.ChatRoom;
import com.team.shopping.Domains.SiteUser;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepositoryCustom {
    Optional<ChatRoom> getChatRoom(SiteUser sendUser, SiteUser acceptUser);
    List<ChatRoom> getChatRoomList(String username);
}
