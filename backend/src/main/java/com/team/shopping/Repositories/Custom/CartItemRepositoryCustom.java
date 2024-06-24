package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.CartItem;
import com.team.shopping.Domains.Product;

import java.util.Optional;

public interface CartItemRepositoryCustom {
    Optional<CartItem> findByProduct(Product product);
}
