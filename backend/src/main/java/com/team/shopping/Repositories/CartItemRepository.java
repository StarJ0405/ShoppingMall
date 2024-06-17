package com.team.shopping.Repositories;

import com.team.shopping.Domains.CartItem;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByUserOrderByCreateDateDesc(SiteUser user);
    CartItem findByUser(SiteUser user);
    CartItem findByUserAndProduct(SiteUser user, Product product);
    void deleteByProduct(Product product);
}
