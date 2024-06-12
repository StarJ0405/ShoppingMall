package com.team.shopping.Services.Module;

import com.team.shopping.Domains.OptionList;
import com.team.shopping.Domains.Product;
import com.team.shopping.Repositories.OptionListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptionListService {
    private final OptionListRepository optionListRepository;

    public OptionList save(String name, Product product) {
        return optionListRepository.save(OptionList.builder()
                .name(name)
                .product(product)
                .build());
    }
}
