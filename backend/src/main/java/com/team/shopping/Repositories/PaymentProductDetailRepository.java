package com.team.shopping.Repositories;

import com.team.shopping.Domains.PaymentProduct;
import com.team.shopping.Domains.PaymentProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentProductDetailRepository extends JpaRepository<PaymentProductDetail, Long> {

    List<PaymentProductDetail> findByPaymentProduct (PaymentProduct paymentProduct);
}
