package com.team.shopping.Repositories;

import com.team.shopping.Domains.PaymentLog;
import com.team.shopping.Domains.PaymentProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentProductRepository extends JpaRepository<PaymentProduct, Long> {

    List<PaymentProduct> findByPaymentLog (PaymentLog paymentLog);
}
