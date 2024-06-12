package com.team.shopping.Services.Module;

import com.team.shopping.Domains.*;
import com.team.shopping.Repositories.PaymentProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentProductService {

    private final PaymentProductRepository paymentProductRepository;

    public List<PaymentProduct> getList (PaymentLog paymentLog) {
        return this.paymentProductRepository.findByPaymentLog(paymentLog);
    }

    public PaymentProduct save (PaymentLog paymentLog, Product product, CartItem cartItem) {
        return this.paymentProductRepository.save(PaymentProduct.builder()
                .paymentLog(paymentLog)
                .productId(product.getId())
                .seller(product.getSeller())
                .price(product.getPrice())
                .url(product.getDescription())
                .title(product.getTitle())
                .brand(product.getBrand())
                .count(cartItem.getCount())
                .build());
    }
}
