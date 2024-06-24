package com.team.shopping.Repositories;

import com.team.shopping.Domains.Event;
import com.team.shopping.Domains.EventProduct;
import com.team.shopping.Repositories.Custom.EventProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventProductRepository extends JpaRepository<EventProduct, Long>, EventProductRepositoryCustom {

    List<EventProduct> findByEvent(Event event);
}
