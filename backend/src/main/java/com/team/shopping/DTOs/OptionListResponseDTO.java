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

    private List<OptionResponseDTO> optionResponseDTOList;

    @Builder
    public OptionListResponseDTO (OptionList optionList, List<OptionResponseDTO> optionResponseDTOList) {
        this.optionListId = optionList.getId();
        this.optionListName = optionList.getName();
        this.optionResponseDTOList = optionResponseDTOList;
    }
}
