package com.team.shopping.Repositories;

import com.team.shopping.Domains.Address;
import com.team.shopping.Domains.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(SiteUser user);
}
