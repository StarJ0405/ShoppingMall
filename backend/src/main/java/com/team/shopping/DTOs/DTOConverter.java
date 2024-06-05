package com.team.shopping.DTOs;

import com.team.shopping.Domains.Product;
import com.team.shopping.Domains.WishList;

import java.util.List;
import java.util.stream.Collectors;

public class DTOConverter {

    public static ProductResponseDTO toProductResponseDTO(Product product) {

        return ProductResponseDTO.builder()
                .id(product.getId())
                .count(product.getCount())
                .a_s(product.getA_s())
                .brand(product.getBrand())
                .price(product.getPrice())
                .categoryName(product.getCategory().getName())
                .title(product.getTitle())
                .authorUsername(product.getAuthor().getUsername())      // product --> productResponseDTO 로 변환
                .detail(product.getDetail())
                .receipt(product.getReceipt())
                .delivery(product.getDelivery())
                .description(product.getDescription())
                .address(product.getAddress())
                .dateLimit(product.getDateLimit())
                .build();
    }

    public static List<ProductResponseDTO> toProductResponseDTOList(WishList wishList) {

        List<ProductResponseDTO> productResponseDTOList = null;         // productResponseDTOList 초기화

        if (wishList.getProductList() != null) {
            productResponseDTOList = wishList.getProductList().stream() // wishList 객체를 받아서 productList 를
                                                                        // ProductResponseDTO 로 변환 선언
                    .map(DTOConverter::toProductResponseDTO)          // DTOConverter 클래스의 toProductResponseDTO 메서드 형식으로
                                                                        // 스트림을 사용하여 productList 의 product 객체 변환
                    .collect(Collectors.toList());                      // List 형식으로 수집
        }

        return productResponseDTOList;
    }

}
