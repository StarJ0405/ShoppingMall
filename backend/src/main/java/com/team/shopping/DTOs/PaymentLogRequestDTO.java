package com.team.shopping.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaymentLogRequestDTO {

    private List<Long> cartItemIdList;

}
