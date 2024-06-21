package com.team.shopping.Repositories;

import com.team.shopping.Domains.OptionList;
import com.team.shopping.Domains.Options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionsRepository extends JpaRepository<Options, Long> {

    List<Options> findByOptionList(OptionList optionList);
}
