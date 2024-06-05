package com.team.shopping.DTOs;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.WishList;

import java.util.List;
import java.util.stream.Collectors;

public class DTOConverter {

    public static ProductResponseDTO toProductDTO(Product product) {
        String categoryName = null;
        if (product.getCategory() != null) {
            categoryName = product.getCategory().getName();
        }

        return ProductResponseDTO.builder()
                .id(product.getId())
                .count(product.getCount())
                .a_s(product.getA_s())
                .brand(product.getBrand())
                .price(product.getPrice())
                .categoryName(categoryName)
                .title(product.getTitle())
                .authorUsername(product.getAuthor().getUsername())
                .detail(product.getDetail())
                .receipt(product.getReceipt())
                .delivery(product.getDelivery())
                .description(product.getDescription())
                .address(product.getAddress())
                .dateLimit(product.getDateLimit())
                .build();
    }

    public static WishListResponseDTO toWishListResponseDTO(WishList wishList) {
        List<ProductResponseDTO> productResponseDTOList = null;
        if (wishList.getProductList() != null) {
            productResponseDTOList = wishList.getProductList().stream()
                    .map(DTOConverter::toProductDTO)
                    .collect(Collectors.toList());
        }

        return new WishListResponseDTO(
                wishList.getId(),
                wishList.getUser().getUsername(),
                productResponseDTOList
        );
    }

}
