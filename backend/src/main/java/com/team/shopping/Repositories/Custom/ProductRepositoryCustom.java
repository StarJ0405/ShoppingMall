package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> findAllPage(Pageable pageable);
}
