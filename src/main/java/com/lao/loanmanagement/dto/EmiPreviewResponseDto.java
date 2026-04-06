package com.lao.loanmanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmiPreviewResponseDto {

    private Double emi;
    private Double totalPayable;
    private Double totalInterest;
}
