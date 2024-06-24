package com.team.shopping.Repositories.Custom;

import com.team.shopping.Domains.OptionList;
import com.team.shopping.Domains.Options;
import com.team.shopping.Domains.Product;

import java.util.Optional;

public interface OptionsRepositoryCustom {
    Optional<Options> getOptionByOptionList(OptionList optionList);
}
