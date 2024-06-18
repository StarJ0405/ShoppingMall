package com.team.shopping.DTOs;

import com.team.shopping.Domains.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DTOConverter {
    public static OptionResponseDTO toOptionResponseDTO (Options options) {

        return new OptionResponseDTO(options);
    }

    public static CartResponseDTO toCartResponseDTO(CartItem cartItem, List<CartItemDetail> cartItemDetails) {

        LocalDateTime _createDate = cartItem.getCreateDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dateTimeString = _createDate.format(formatter);
        Long createDate = Long.parseLong(dateTimeString);

        List<OptionResponseDTO> optionResponseDTOList = new ArrayList<>();
        int totalPrice = cartItem.getProduct().getPrice() * cartItem.getCount();

        for (CartItemDetail cartItemDetail : cartItemDetails) {
            Options option = cartItemDetail.getOptions();
            optionResponseDTOList.add(DTOConverter.toOptionResponseDTO(option));
            totalPrice += (option.getPrice() * option.getCount()) * cartItem.getCount();
        }

        return CartResponseDTO.builder()
                .optionResponseDTOList(optionResponseDTOList)
                .cartItem(cartItem)
                .totalPrice(totalPrice)
                .createDate(createDate)
                .build();
    }


    public static PaymentProductDetailResponseDTO toPaymentProductDetailResponseDTO (PaymentProductDetail paymentProductDetail) {
        return new PaymentProductDetailResponseDTO(paymentProductDetail);
    }

    public static PaymentProductResponseDTO toPaymentProductResponseDTO (PaymentProduct paymentProduct, List<PaymentProductDetail> paymentProductDetailList) {
        List<PaymentProductDetailResponseDTO> paymentProductDetailResponseDTOList = new ArrayList<>();
        int withOptionPrice = paymentProduct.getPrice() * paymentProduct.getCount();
        for (PaymentProductDetail paymentProductDetail : paymentProductDetailList) {
            paymentProductDetailResponseDTOList.add(DTOConverter.toPaymentProductDetailResponseDTO(paymentProductDetail));
            withOptionPrice += paymentProductDetail.getOptionPrice() * paymentProductDetail.getOptionCount() * paymentProduct.getCount();
        }
        return PaymentProductResponseDTO.builder()
                .paymentProduct(paymentProduct)
                .withOptionPrice(withOptionPrice)
                .paymentProductDetailResponseDTOList(paymentProductDetailResponseDTOList)
                .build();
    }

    public static PaymentLogResponseDTO toPaymentLogResponseDTO(PaymentLog paymentLog,
                                                                List<PaymentProductResponseDTO> paymentProductResponseDTOList)  {
        LocalDateTime createDate = paymentLog.getCreateDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dateTimeString = createDate.format(formatter);
        Long paymentDate = Long.parseLong(dateTimeString);

        int totalPrice = 0;
        for (PaymentProductResponseDTO paymentProductResponseDTO : paymentProductResponseDTOList) {
            totalPrice += paymentProductResponseDTO.getWithOptionPrice();
        }
        return PaymentLogResponseDTO.builder()
                .totalPrice(totalPrice)
                .paymentLog(paymentLog)
                .paymentProductResponseDTOList(paymentProductResponseDTOList)
                .paymentDate(paymentDate)
                .build();
    }

}
