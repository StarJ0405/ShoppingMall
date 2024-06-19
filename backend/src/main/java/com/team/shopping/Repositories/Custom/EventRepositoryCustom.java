package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.Event;
import com.team.shopping.Domains.Product;

public interface EventRepositoryCustom {

    Event findByProduct (Product product);
}
