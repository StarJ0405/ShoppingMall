package com.team.shopping.Repositories;

import com.team.shopping.Domains.PaymentProduct;
import com.team.shopping.Domains.PaymentProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PaymentProductDetailRepository extends JpaRepository<PaymentProductDetail, Long> {

    List<PaymentProductDetail> findByPaymentProduct (PaymentProduct paymentProduct);
}
