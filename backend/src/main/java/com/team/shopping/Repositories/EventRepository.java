package com.team.shopping.Repositories;

import com.team.shopping.Domains.Event;
import com.team.shopping.Repositories.Custom.EventRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
}
