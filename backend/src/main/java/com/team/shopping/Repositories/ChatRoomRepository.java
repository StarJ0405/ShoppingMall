package com.team.shopping.Repositories;

import com.team.shopping.Domains.ChatRoom;
import com.team.shopping.Repositories.Custom.ChatRoomRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
}
