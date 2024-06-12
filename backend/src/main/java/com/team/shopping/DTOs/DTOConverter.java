package com.team.shopping.DTOs;

import com.team.shopping.Domains.*;

import java.util.ArrayList;
import java.util.List;

public class DTOConverter {
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

    public static PaymentProductDetailResponseDTO toPaymentProductDetailResponseDTO (PaymentProductDetail paymentProductDetail) {
        return new PaymentProductDetailResponseDTO(paymentProductDetail);
    }

    public static PaymentProductResponseDTO toPaymentProductResponseDTO (PaymentProduct paymentProduct, List<PaymentProductDetail> paymentProductDetailList) {
        List<PaymentProductDetailResponseDTO> paymentProductDetailResponseDTOList = new ArrayList<>();
        for (PaymentProductDetail paymentProductDetail : paymentProductDetailList) {
            paymentProductDetailResponseDTOList.add(DTOConverter.toPaymentProductDetailResponseDTO(paymentProductDetail));
        }
        return new PaymentProductResponseDTO(paymentProduct, paymentProductDetailResponseDTOList);
    }

    public static PaymentLogResponseDTO toPaymentLogResponseDTO(PaymentLog paymentLog, List<PaymentProductResponseDTO> paymentProductResponseDTOList) {
        int totalPrice = 0;
        for (PaymentProductResponseDTO paymentProductResponseDTO : paymentProductResponseDTOList) {
            totalPrice += paymentProductResponseDTO.getPrice() * paymentProductResponseDTO.getCount();

            for (PaymentProductDetailResponseDTO paymentProductDetailResponseDTO : paymentProductResponseDTO.getPaymentProductDetailResponseDTOList()) {
                totalPrice += paymentProductDetailResponseDTO.getOptionPrice() * paymentProductDetailResponseDTO.getOptionCount();
            }
        }
        return PaymentLogResponseDTO.builder()
                .price(totalPrice)
                .paymentLog(paymentLog)
                .paymentProductResponseDTOList(paymentProductResponseDTOList)
                .build();
    }




}
