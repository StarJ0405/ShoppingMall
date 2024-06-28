package com.team.shopping.DTOs;

import com.team.shopping.Domains.OptionList;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OptionListResponseDTO {

    private Long optionListId;

    private String optionListName;
    private String name;
    private List<OptionResponseDTO> optionResponseDTOList;
    private List<OptionRequestDTO> child;

    @Builder
    public OptionListResponseDTO (OptionList optionList, List<OptionResponseDTO> optionResponseDTOList) {
        this.optionListId = optionList.getId();
        this.optionListName = optionList.getName();
        this.name=optionListName;
        this.optionResponseDTOList = optionResponseDTOList;
        this.child = optionResponseDTOList.stream().map(option->OptionRequestDTO.builder().name(option.getOptionName()).price(option.getOptionPrice()).count(option.getOptionRemain()).build()).toList();

    }
}
