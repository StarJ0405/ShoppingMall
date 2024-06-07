package com.team.shopping.DTOs;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.Wish;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DTOConverter {

    public static ProductResponseDTO toProductResponseDTO(Product product) {

        return ProductResponseDTO.builder()
                .id(product.getId())
                .count(product.getRemain())
                .a_s(product.getA_s())
                .brand(product.getBrand())
                .price(product.getPrice())
                .categoryName(product.getCategory().getName())
                .title(product.getTitle())
                .authorUsername(product.getSeller().getUsername())      // product --> productResponseDTO 로 변환
                .detail(product.getDetail())
                .receipt(product.getReceipt())
                .delivery(product.getDelivery())
                .description(product.getDescription())
                .address(product.getAddress())
                .dateLimit(product.getDateLimit())
                .build();
    }

    public static List<ProductResponseDTO> toProductResponseDTOList(List<Wish> wishList) {

        List<ProductResponseDTO> productResponseDTOList;
        List<Product> productList = new ArrayList<>();

        for (Wish _wishList : wishList) {
            Product product = _wishList.getProduct();
            productList.add(product);
        }
        productResponseDTOList = productList.stream()
                .map(DTOConverter::toProductResponseDTO)
                .collect(Collectors.toList());

        return productResponseDTOList;
    }

}
