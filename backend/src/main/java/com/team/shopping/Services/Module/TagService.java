package com.team.shopping.Services.Module;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.Tag;
import com.team.shopping.Repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public void save(String tagName, Product product) {
        tagRepository.save(Tag.builder()
                .name(tagName)
                .product(product)
                .build());

    }

    public List<String> findByProduct(Product product) {
        return tagRepository.findTagNamesByProduct(product);
    }
}
