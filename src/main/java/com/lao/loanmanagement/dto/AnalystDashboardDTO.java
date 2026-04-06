package com.lao.loanmanagement.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalystDashboardDTO {

    private long totalLoans;

    private long appliedLoans;
    private long approvedLoans;
    private long fundedLoans;
    private long activeLoans;
    private long closedLoans;

    private double totalLoanAmount;
    private double averageLoanAmount;
}
