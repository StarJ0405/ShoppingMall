package com.team.shopping.DTOs;

import com.team.shopping.Domains.Options;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OptionResponseDTO {

    private Long optionId;

    private String optionName;

    private int optionCount;

    private int optionPrice;

    @Builder
    public OptionResponseDTO (Options options) {
        this.optionId = options.getId();
        this.optionName = options.getName();
        this.optionCount = options.getCount();
        this.optionPrice = options.getPrice();
    }
}
