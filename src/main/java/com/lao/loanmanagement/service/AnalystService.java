package com.lao.loanmanagement.service;

import com.lao.loanmanagement.dto.AnalystDashboardDTO;
import com.lao.loanmanagement.dto.LoanStatusCountDTO;
import com.lao.loanmanagement.dto.MonthlyLoanStatsDTO;

import java.util.List;

public interface AnalystService {

    // ===============================
    // 1️⃣ DASHBOARD SUMMARY (KPI CARDS)
    // ===============================
    AnalystDashboardDTO getDashboardSummary();

    // =================================
    // 2️⃣ LOAN STATUS CHART DATA
    // =================================
    List<LoanStatusCountDTO> getLoanStatusCounts();

    // ==================================
    // 3️⃣ MONTHLY LOAN TRENDS DATA
    // ==================================
    List<MonthlyLoanStatsDTO> getMonthlyLoanStats();
}
