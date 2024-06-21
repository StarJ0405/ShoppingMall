package com.team.shopping.Services.Module;

import com.team.shopping.Domains.Options;
import com.team.shopping.Domains.PaymentProduct;
import com.team.shopping.Domains.PaymentProductDetail;
import com.team.shopping.Repositories.PaymentProductDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentProductDetailService {

    private final PaymentProductDetailRepository paymentProductDetailRepository;

    public List<PaymentProductDetail> getList (PaymentProduct paymentProduct) {
        return this.paymentProductDetailRepository.findByPaymentProduct(paymentProduct);
    }

    public PaymentProductDetail save (PaymentProduct paymentProduct, Options option) {
        return this.paymentProductDetailRepository.save(PaymentProductDetail.builder()
                .optionListName(option.getOptionList().getName())
                .paymentProduct(paymentProduct)
                .optionName(option.getName())
                .optionCount(1)
                .optionPrice(option.getPrice())
                .build());
    }
}
