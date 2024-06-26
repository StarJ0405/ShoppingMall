package com.team.shopping.DTOs;

import com.team.shopping.Domains.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class DTOConverter {
    public static OptionResponseDTO toOptionResponseDTO (Options options) {

        return new OptionResponseDTO(options);
    }

    public static CartItemDetailResponseDTO toCartItemDetailResponseDTO (Options options) {

        return new CartItemDetailResponseDTO(options);
    }

    public static CartResponseDTO toCartResponseDTO(CartItem cartItem, List<CartItemDetail> cartItemDetails,
                                                    Double discount, int discountPrice, String imageUrl) {

        LocalDateTime _createDate = cartItem.getCreateDate();
        Long createDate = dateTimeTransfer(_createDate);

        List<CartItemDetailResponseDTO> cartItemDetailResponseDTOList = new ArrayList<>();
        long totalPrice = 0L;
        totalPrice += (long)discountPrice * cartItem.getCount();

        for (CartItemDetail cartItemDetail : cartItemDetails) {
            Options option = cartItemDetail.getOptions();
            cartItemDetailResponseDTOList.add(DTOConverter.toCartItemDetailResponseDTO(option));
            totalPrice += ((long) option.getPrice() * cartItem.getCount());
        }

        return CartResponseDTO.builder()
                .cartItemDetailResponseDTOList(cartItemDetailResponseDTOList)
                .cartItem(cartItem)
                .discount(discount)
                .discountPrice(discountPrice)
                .totalPrice(totalPrice)
                .createDate(createDate)
                .imageUrl(imageUrl)
                .build();
    }


    public static PaymentProductDetailResponseDTO toPaymentProductDetailResponseDTO (PaymentProductDetail paymentProductDetail) {
        return new PaymentProductDetailResponseDTO(paymentProductDetail);
    }

    public static PaymentProductResponseDTO toPaymentProductResponseDTO (PaymentProduct paymentProduct,
                                                                         List<PaymentProductDetail> paymentProductDetailList,
                                                                         String imageUrl, ReviewResponseDTO reviewResponseDTO) {
        List<PaymentProductDetailResponseDTO> paymentProductDetailResponseDTOList = new ArrayList<>();

        double discount = (paymentProduct.getDiscount() != null) ? paymentProduct.getDiscount() : 0.0;
        double discountedPrice = paymentProduct.getPrice() * (1 - discount / 100);
        discountedPrice = Math.round(discountedPrice * 10) / 10.0;
        int discountPrice = (int) Math.round(discountedPrice);

        int withOptionPrice = discountPrice * paymentProduct.getCount();
        for (PaymentProductDetail paymentProductDetail : paymentProductDetailList) {
            paymentProductDetailResponseDTOList.add(DTOConverter.toPaymentProductDetailResponseDTO(paymentProductDetail));
            withOptionPrice += paymentProductDetail.getOptionPrice() * paymentProduct.getCount();
        }
        return PaymentProductResponseDTO.builder()
                .paymentProduct(paymentProduct)
                .withOptionPrice(withOptionPrice)
                .discountPrice(discountPrice)
                .imageUrl(imageUrl)
                .paymentProductDetailResponseDTOList(paymentProductDetailResponseDTOList)
                .responseDTO(reviewResponseDTO)
                .build();
    }

    public static PaymentLogResponseDTO toPaymentLogResponseDTO(PaymentLog paymentLog,
                                                                List<PaymentProductResponseDTO> paymentProductResponseDTOList)  {
        LocalDateTime createDate = paymentLog.getCreateDate();
        Long paymentDate = dateTimeTransfer(createDate);

        long totalPrice = 0L;
        for (PaymentProductResponseDTO paymentProductResponseDTO : paymentProductResponseDTOList) {
            totalPrice += paymentProductResponseDTO.getWithOptionPrice();
        }
        totalPrice -= paymentLog.getUsedPoint();
        return PaymentLogResponseDTO.builder()
                .totalPrice(totalPrice)
                .paymentLog(paymentLog)
                .paymentProductResponseDTOList(paymentProductResponseDTOList)
                .paymentDate(paymentDate)
                .build();
    }
    private static Long dateTimeTransfer (LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
