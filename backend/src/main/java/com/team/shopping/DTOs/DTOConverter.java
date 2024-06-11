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

    public static PaymentLogResponseDTO toPaymentLogResponseDTO(PaymentLog paymentLog, List<PaymentProduct> paymentProductList) {
        int totalPrice = 0;
        for (PaymentProduct paymentProduct : paymentProductList) {
            totalPrice += paymentProduct.getPrice() * paymentProduct.getCount();
        }
        return PaymentLogResponseDTO.builder()
                .paymentStatus(paymentLog.getPaymentStatus().toString())
                .url(paymentProductList.getFirst().getUrl())
                .productTitle(paymentProductList.getFirst().getTitle() + " 외 " + paymentProductList.size() + "개 상품")
                .price(totalPrice)
                .build();
    }
}
