package com.team.shopping.Services.Module;

import com.team.shopping.DTOs.ProductCreateRequestDTO;
import com.team.shopping.Domains.Category;
import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.SiteUser;
import com.team.shopping.Enums.Sorts;
import com.team.shopping.Repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product save (Product product) {
        return this.productRepository.save(product);
    }

    @Transactional
    public Product getProduct (Long productId) {
        return this.productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("not found product"));
    }
    public void Update(Product product, String detail){
        product.setDetail(detail);
        this.productRepository.save(product);
    }
    public Product saveProduct(ProductCreateRequestDTO requestDTO, SiteUser user, Category category) {
        return this.productRepository.save(Product.builder()
                .seller(user)
                .category(category)
                .price(requestDTO.getPrice())
                .description(requestDTO.getDescription())
                .detail(requestDTO.getDetail())
                .dateLimit(requestDTO.getDateLimit())
                .remain(requestDTO.getRemain())
                .title(requestDTO.getTitle())
                .delivery(requestDTO.getDelivery())
                .address(requestDTO.getAddress())
                .receipt(requestDTO.getReceipt())
                .a_s(requestDTO.getA_s())
                .brand(requestDTO.getBrand())
                .build());
    }

    public List<Product> getProductList() {
        return this.productRepository.findAll();
    }

    public void deleteProduct (Product product) {
        this.productRepository.delete(product);
    }

    public Page<Product> getLatestList(Pageable pageable) {
        return this.productRepository.findAllPage(pageable);
    }

    public Page<Product> searchByKeyword(Pageable pageable, String keyword, Sorts sorts) {
        return this.productRepository.searchByKeyword(pageable, keyword, sorts);
    }
    public Page<Product> categorySearchByTitle (Pageable pageable, String keyword, Sorts sorts, Long categoryId) {
        return this.productRepository.findByTitleOrTagGroupByCategory(pageable, keyword,categoryId, sorts);
    }

    public List<Product> getMyList(SiteUser user) {
        return this.productRepository.findBySeller(user);
    }

    public Product updateProduct(Product product, ProductCreateRequestDTO requestDTO, Category category) {
        product.setTitle(requestDTO.getTitle());
        product.setDetail(requestDTO.getDetail());
        product.setRemain(requestDTO.getRemain());
        product.setPrice(requestDTO.getPrice());
        product.setBrand(requestDTO.getBrand());
        product.setA_s(requestDTO.getA_s());
        product.setAddress(requestDTO.getAddress());
        product.setCategory(category);
        product.setDateLimit(requestDTO.getDateLimit());
        product.setDelivery(requestDTO.getDelivery());
        product.setModifyDate(LocalDateTime.now());
        product.setDescription(requestDTO.getDescription());
        product.setReceipt(requestDTO.getReceipt());
        return productRepository.save(product);
    }
}
