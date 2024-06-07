package com.team.shopping.Services.Module;

import com.team.shopping.DTOs.ProductCreateRequestDTO;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProduct (Long productId) {
        return this.productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다"));
    }

    public Product save(ProductCreateRequestDTO requestDTO, SiteUser user) {
        return this.productRepository.save(Product.builder()
                .seller(user)
                //todo category 기능 만들고 추가해야함.
                .price(requestDTO.getPrice())
                .description(requestDTO.getDescription())
                .detail(requestDTO.getDetail())
                .dateLimit(requestDTO.getDateLimit())
                .count(requestDTO.getCount())
                .title(requestDTO.getTitle())
                .delivery(requestDTO.getDelivery())
                .address(requestDTO.getAddress())
                .receipt(requestDTO.getReceipt())
                .a_s(requestDTO.getA_s())
                .brand(requestDTO.getBrand())
                .build());
    }
}
