package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.EventProduct;
import com.team.shopping.Domains.Product;

import java.util.List;
import java.util.Optional;

public interface EventProductRepositoryCustom {
    List<EventProduct> findByProduct(Product product);
}
