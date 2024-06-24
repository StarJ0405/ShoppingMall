package com.team.shopping.Services.Module;

import com.team.shopping.Domains.OptionList;
import com.team.shopping.Domains.Product;
import com.team.shopping.Repositories.OptionListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OptionListService {
    private final OptionListRepository optionListRepository;

    public List<OptionList> getList (Product product) {
        return this.optionListRepository.findByProduct(product);
    }

    public OptionList save(String name, Product product) {
        return optionListRepository.save(OptionList.builder()
                .name(name)
                .product(product)
                .build());
    }

    public Optional<OptionList> getOptionListByProduct(Product product) {
        return optionListRepository.getOptionListByProduct(product);
    }

    public void delete(OptionList optionList) {
        optionListRepository.delete(optionList);
    }
}
