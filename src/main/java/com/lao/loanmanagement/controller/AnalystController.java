package com.lao.loanmanagement.controller;

import com.lao.loanmanagement.dto.AnalystDashboardDTO;
import com.lao.loanmanagement.dto.LoanStatusCountDTO;
import com.lao.loanmanagement.dto.MonthlyLoanStatsDTO;
import com.lao.loanmanagement.service.AnalystService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analyst")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ANALYST')")
public class AnalystController {

    private final AnalystService analystService;

    // ===============================
    // 1️⃣ DASHBOARD SUMMARY (KPI CARDS)
    // ===============================
    @GetMapping("/dashboard/summary")
    public ResponseEntity<AnalystDashboardDTO> getDashboardSummary() {
        AnalystDashboardDTO summary = analystService.getDashboardSummary();
        return ResponseEntity.ok(summary);
    }

    // =================================
    // 2️⃣ LOAN STATUS CHART (PIE / BAR)
    // =================================
    @GetMapping("/dashboard/loan-status")
    public ResponseEntity<List<LoanStatusCountDTO>> getLoanStatusChart() {
        List<LoanStatusCountDTO> data = analystService.getLoanStatusCounts();
        return ResponseEntity.ok(data);
    }

    // ==================================
    // 3️⃣ MONTHLY LOAN TRENDS (LINE CHART)
    // ==================================
    @GetMapping("/dashboard/monthly-trends")
    public ResponseEntity<List<MonthlyLoanStatsDTO>> getMonthlyLoanTrends() {
        List<MonthlyLoanStatsDTO> trends = analystService.getMonthlyLoanStats();
        return ResponseEntity.ok(trends);
    }
}
