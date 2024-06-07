package com.team.shopping.Repositories;

import com.team.shopping.Domains.CartItem;
import com.team.shopping.Domains.CartItemDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CartItemDetailRepository extends JpaRepository<CartItemDetail, Long> {

    List<CartItemDetail> findAllByCartItem(CartItem cartItem);
    CartItemDetail findByCartItem (CartItem cartItem);
}
