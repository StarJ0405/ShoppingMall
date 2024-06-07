package com.team.shopping.Services.Module;

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
}
