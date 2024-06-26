package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.Event;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.SiteUser;

import java.util.List;

public interface EventRepositoryCustom {

    List<Event> findByProduct (Product product);

    List<Event> findMyList(SiteUser user);
}
