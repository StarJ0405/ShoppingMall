package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.ProductQA;

import java.util.Optional;

public interface ProductQARepositoryCustom {
    Optional<ProductQA> findByProduct(Product product);
}
