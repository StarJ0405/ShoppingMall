package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.Tag;

import java.util.List;

public interface TagRepositoryCustom {
    List<String> findTagNamesByProduct(Product product);

    List<Tag> getTagByProduct(Product product);
}
