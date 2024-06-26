package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.ProductQA;

import java.util.List;

public interface ProductQARepositoryCustom {
    List<ProductQA> findByProduct(Product product);
}
