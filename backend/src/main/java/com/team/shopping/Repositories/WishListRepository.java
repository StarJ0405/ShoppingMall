package com.team.shopping.Repositories;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Domains.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByUserOrderByCreateDateDesc(SiteUser user);
    Wish findByUserAndProduct(SiteUser user, Product product);
    void deleteByProduct(Product product);
}
