package com.team.shopping.DTOs;

import com.team.shopping.Domains.*;

import java.util.List;
import java.util.stream.Collectors;

public class DTOConverter {

    public static ProductResponseDTO toProductResponseDTO(Product product) {
        return new ProductResponseDTO(product);
    }

    public static List<ProductResponseDTO> toProductResponseDTOList(List<WishList> wishList) {

        return wishList.stream()
                .map(WishList::getProduct)
                .map(DTOConverter::toProductResponseDTO)
                .collect(Collectors.toList());
    }

    public static OptionResponseDTO toOptionResponseDTO (Options options) {

        return new OptionResponseDTO(options);
    }

    public static CartResponseDTO toCartResponseDTO(CartItem cartItem, List<CartItemDetail> cartItemDetails) {
        List<OptionResponseDTO> optionResponseDTOList = cartItemDetails.stream()
                .map(CartItemDetail::getOptions)
                .map(DTOConverter::toOptionResponseDTO)
                .collect(Collectors.toList());

        return new CartResponseDTO(cartItem, optionResponseDTOList);
    }
}
