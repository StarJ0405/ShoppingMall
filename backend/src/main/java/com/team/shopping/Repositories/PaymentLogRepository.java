package com.team.shopping.Repositories;

import com.team.shopping.Domains.PaymentLog;
import com.team.shopping.Domains.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentLogRepository extends JpaRepository<PaymentLog, Long> {

    List<PaymentLog> findByOrder (SiteUser order);
}
