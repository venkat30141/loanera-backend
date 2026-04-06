package com.lao.loanmanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowerProfileRequestDto {

    private Long userId;
    private Double monthlyIncome;
    private Integer creditScore;
}
