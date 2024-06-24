package com.team.shopping.Repositories;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Domains.Wish;
import com.team.shopping.Repositories.Custom.WishListRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<Wish, Long>, WishListRepositoryCustom {
    List<Wish> findByUserOrderByCreateDateDesc(SiteUser user);
    Wish findByUserAndProduct(SiteUser user, Product product);
}
