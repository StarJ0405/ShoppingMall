package com.team.shopping.Services.Module;

import com.team.shopping.Domains.OptionList;
import com.team.shopping.Domains.Options;
import com.team.shopping.Repositories.OptionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OptionsService {

    private final OptionsRepository optionsRepository;

    public List<Options> getOptionsList(List<Long> optionIdList) {
        return optionIdList != null ? this.optionsRepository.findAllById(optionIdList) : new ArrayList<>();
    }

    public List<Options> getList(OptionList optionList) {
        return this.optionsRepository.findByOptionList(optionList);
    }

    public Options getOption(Long optionId) {
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

    public Options save(Options options) {
        return this.optionsRepository.save(options);
    }

    public Optional<Options> getOptionByOptionList(OptionList optionList) {
        return optionsRepository.getOptionByOptionList(optionList);
    }

    public void delete(Options options) {
        optionsRepository.delete(options);
    }
}
