package com.team.shopping.Repositories;

import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Domains.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    WishList findByUser (SiteUser user);
}
