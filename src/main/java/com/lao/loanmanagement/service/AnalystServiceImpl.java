package com.lao.loanmanagement.service;

import com.lao.loanmanagement.dto.AnalystDashboardDTO;
import com.lao.loanmanagement.dto.LoanStatusCountDTO;
import com.lao.loanmanagement.dto.MonthlyLoanStatsDTO;
import com.lao.loanmanagement.entity.LoanStatus;
import com.lao.loanmanagement.repository.LoanRepository;
import com.lao.loanmanagement.service.AnalystService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalystServiceImpl implements AnalystService {

    private final LoanRepository loanRepository;

    @Override
    public AnalystDashboardDTO getDashboardSummary() {

        long totalLoans = loanRepository.count();
        long appliedLoans = loanRepository.countByStatus(LoanStatus.APPLIED);
        long approvedLoans = loanRepository.countByStatus(LoanStatus.APPROVED);
        long fundedLoans = loanRepository.countByStatus(LoanStatus.FUNDED);
        long activeLoans = loanRepository.countByStatus(LoanStatus.ACTIVE);
        long closedLoans = loanRepository.countByStatus(LoanStatus.CLOSED);

        Double totalLoanAmount = loanRepository.getTotalLoanAmount();
        Double averageLoanAmount = loanRepository.getAverageLoanAmount();

        return AnalystDashboardDTO.builder()
                .totalLoans(totalLoans)
                .appliedLoans(appliedLoans)
                .approvedLoans(approvedLoans)
                .fundedLoans(fundedLoans)
                .activeLoans(activeLoans)
                .closedLoans(closedLoans)
                .totalLoanAmount(totalLoanAmount != null ? totalLoanAmount : 0.0)
                .averageLoanAmount(averageLoanAmount != null ? averageLoanAmount : 0.0)
                .build();
    }

    @Override
    public List<LoanStatusCountDTO> getLoanStatusCounts() {

        List<Object[]> results = loanRepository.countLoansByStatus();
        List<LoanStatusCountDTO> response = new ArrayList<>();

        for (Object[] row : results) {
            LoanStatus status = (LoanStatus) row[0];
            Long count = (Long) row[1];
            response.add(new LoanStatusCountDTO(status.name(), count));
        }

        return response;
    }

    @Override
    public List<MonthlyLoanStatsDTO> getMonthlyLoanStats() {

        List<Object[]> results = loanRepository.countLoansByMonth();
        List<MonthlyLoanStatsDTO> response = new ArrayList<>();

        for (Object[] row : results) {
            Integer month = (Integer) row[0];
            Long count = (Long) row[1];
            response.add(new MonthlyLoanStatsDTO(month, count));
        }

        return response;
    }
}
