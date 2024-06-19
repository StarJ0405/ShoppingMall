package com.team.shopping.DTOs;

import com.team.shopping.Domains.Event;
import com.team.shopping.Domains.SiteUser;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EventResponseDTO {

    private Long eventId;

    private Long startDate;

    private Long endDate;

    private Double discount;

    private List<ProductResponseDTO> productResponseDTOList;

    private String creator;

    private Long createDate;

    private Long modifyDate;

    @Builder
    public EventResponseDTO (SiteUser user, Event event, List<ProductResponseDTO> productResponseDTOList,
                             Long startDate, Long endDate, Long createDate, Long modifyDate) {
        this.eventId = event.getId();
        this.startDate = startDate;
        this.endDate = endDate;
        this.discount = event.getDiscount();
        this.productResponseDTOList = productResponseDTOList;
        this.creator = user.getNickname();
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }
}
