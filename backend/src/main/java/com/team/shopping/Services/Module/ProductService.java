package com.team.shopping.Services.Module;

import com.team.shopping.DTOs.ProductRequestDTO;
import com.team.shopping.Domains.Product;
import com.team.shopping.Repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProduct (ProductRequestDTO productRequestDTO) {
        return this.productRepository.findById(productRequestDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다. product_id = " + productRequestDTO.getProductId()));
    }

}
