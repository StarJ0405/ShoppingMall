package com.team.shopping.Repositories;

import com.team.shopping.Domains.CartItem;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.Custom.CartItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>, CartItemRepositoryCustom {
    List<CartItem> findAllByUserOrderByCreateDateDesc(SiteUser user);
    List<CartItem> findByUserAndProduct(SiteUser user, Product product);
}
