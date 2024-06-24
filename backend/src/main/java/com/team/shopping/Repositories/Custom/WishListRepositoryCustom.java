package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.Wish;

import java.util.Optional;

public interface WishListRepositoryCustom {
    Optional<Wish> findByProduct(Product product);
}
