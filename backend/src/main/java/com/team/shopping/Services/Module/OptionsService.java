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

    public void saveOption(int count, String name, int price, OptionList optionList) {
        if (count <= 0) {
            count = 1;
        }
        optionsRepository.save(Options.builder()
                        .count(count)
                        .name(name)
                        .price(price)
                        .optionList(optionList)
                .build());
    }
    public Options save (Options options) {
        return this.optionsRepository.save(options);
    }
}
