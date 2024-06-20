package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.Event;
import com.team.shopping.Domains.Product;

import java.util.List;

public interface EventRepositoryCustom {

    List<Event> findByProduct (Product product);
}
