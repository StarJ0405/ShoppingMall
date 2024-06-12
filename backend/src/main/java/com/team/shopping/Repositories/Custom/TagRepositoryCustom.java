package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.Product;

import java.util.List;

public interface TagRepositoryCustom {
    List<String> findTagNamesByProduct(Product product);
}
