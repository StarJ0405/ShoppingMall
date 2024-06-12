package com.team.shopping.Services.Module;

import com.team.shopping.Domains.OptionList;
import com.team.shopping.Domains.Options;
import com.team.shopping.Repositories.OptionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionsService {

    private final OptionsRepository optionsRepository;

    public List<Options> getOptionsList (List<Long> optionIdList) {
        return this.optionsRepository.findAllById(optionIdList);
    }

    public Options getOption (Long optionId) {
        return this.optionsRepository.findById(optionId).orElseThrow();
    }

    public void save(int count, String name, int price, OptionList optionList) {
        optionsRepository.save(Options.builder()
                        .count(count)
                        .name(name)
                        .price(price)
                        .optionList(optionList)
                .build());
    }
}
