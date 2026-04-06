package com.lao.loanmanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanRequestDto {

    private Long borrowerId;
    private Double amount;
    private Double interestRate;
    private Integer tenureMonths;
}
