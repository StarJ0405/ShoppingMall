package com.team.shopping.Repositories;

import com.team.shopping.Domains.PaymentLog;
import com.team.shopping.Domains.PaymentProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PaymentProductRepository extends JpaRepository<PaymentProduct, Long> {

    List<PaymentProduct> findByPaymentLog (PaymentLog paymentLog);
}
