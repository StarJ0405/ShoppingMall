package com.team.shopping.DTOs;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EventRequestDTO {

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Double discount;

    private Long ProductId;

}
