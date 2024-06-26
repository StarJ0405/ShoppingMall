package com.team.shopping.Repositories;

import com.team.shopping.Domains.ChatMessage;
import com.team.shopping.Repositories.Custom.ChatMessageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageRepositoryCustom {
}
