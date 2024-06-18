package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.Product;
import com.team.shopping.Enums.Sorts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> findAllPage(Pageable pageable);

    Page<Product> searchByKeyword(Pageable pageable, String keyword, Sorts sorts);

    Page<Product> findByTitleOrTagGroupByCategory(Pageable pageable, String keyword, Long categoryId, Sorts sorts);
}