package com.team.shopping.Repositories;

import com.team.shopping.Domains.OptionList;
import com.team.shopping.Domains.Product;
import com.team.shopping.Repositories.Custom.OptionListRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionListRepository extends JpaRepository<OptionList, Long>, OptionListRepositoryCustom {
    List<OptionList> findByProduct(Product product);
}
