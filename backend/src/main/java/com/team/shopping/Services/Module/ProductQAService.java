package com.team.shopping.Services.Module;


import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.ProductQA;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.ProductQARepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductQAService {
    private final ProductQARepository productQARepository;


    @Transactional
    public void save(String value, String content, SiteUser user, Product product) {
        this.productQARepository.save(ProductQA.builder()
                .title(value)
                .product(product)
                .author(user)
                .content(content)
                .build());
    }

    public void update(String answer, SiteUser user, ProductQA productQA) {
        productQA.setSeller(user);
        productQA.setAnswer(answer);
        productQARepository.save(productQA);
    }

    public Optional<ProductQA> getProductQA(Long productQAId) {
        return productQARepository.findById(productQAId);
    }

    public List<ProductQA> findByProduct(Product product) {
        return productQARepository.findByProduct(product);
    }

    public void delete(ProductQA productQA) {
        productQARepository.delete(productQA);
    }
}
