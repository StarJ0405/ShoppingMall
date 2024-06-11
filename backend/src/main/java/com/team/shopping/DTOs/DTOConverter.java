package com.team.shopping.DTOs;

import com.team.shopping.Domains.*;

import java.util.ArrayList;
import java.util.List;

public class DTOConverter {

    public static ProductResponseDTO toProductResponseDTO(Product product) {
        return new ProductResponseDTO(product);
    }

    public static List<ProductResponseDTO> toProductResponseDTOList(List<Wish> wishList) {
        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        for (Wish wish : wishList) {
            productResponseDTOList.add(toProductResponseDTO(wish.getProduct()));
        }
        return productResponseDTOList;
    }


    public static OptionResponseDTO toOptionResponseDTO (Options options) {

        return new OptionResponseDTO(options);
    }

    public static CartResponseDTO toCartResponseDTO(CartItem cartItem, List<CartItemDetail> cartItemDetails) {
        List<OptionResponseDTO> optionResponseDTOList = new ArrayList<>();
        for (CartItemDetail cartItemDetail : cartItemDetails) {
            optionResponseDTOList.add(DTOConverter.toOptionResponseDTO(cartItemDetail.getOptions()));
        }

        return new CartResponseDTO(cartItem, optionResponseDTOList);
    }
}
