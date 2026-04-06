package com.lao.loanmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyLoanStatsDTO {

    private int month;      // 1 = Jan, 2 = Feb, ...
    private long loanCount;
}
