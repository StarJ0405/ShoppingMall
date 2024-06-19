package com.team.shopping.Repositories;

import com.team.shopping.Domains.EventProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventProduct, Long> {
}
