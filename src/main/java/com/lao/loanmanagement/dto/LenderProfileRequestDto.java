package com.lao.loanmanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LenderProfileRequestDto {

    private Long userId;
    private Double availableFunds;
}
