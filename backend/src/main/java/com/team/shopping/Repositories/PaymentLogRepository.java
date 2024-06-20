package com.team.shopping.Repositories;

import com.team.shopping.Domains.PaymentLog;
import com.team.shopping.Domains.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PaymentLogRepository extends JpaRepository<PaymentLog, Long> {

    List<PaymentLog> findByOrderOrderByCreateDateDesc(SiteUser order);
}
