package com.lao.loanmanagement.repository;

import com.lao.loanmanagement.entity.Loan;
import com.lao.loanmanagement.entity.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    // =====================
    // EXISTING METHODS (DO NOT TOUCH)
    // =====================

    List<Loan> findByBorrowerId(Long borrowerId);

    List<Loan> findByLenderId(Long lenderId);

    List<Loan> findByStatus(LoanStatus status);

    // ✅ loans assigned to lender (APPROVED or ACTIVE)
    List<Loan> findByLenderIdAndStatusIn(Long lenderId, List<LoanStatus> statuses);

    // =====================
    // 🔥 NEW METHODS FOR ANALYST DASHBOARD (ONLY ADDITIONS)
    // =====================

    // ✅ Count loans by status (KPI cards)
    long countByStatus(LoanStatus status);

    // ✅ Status-wise loan count (Pie / Bar chart)
    @Query("""
        SELECT l.status, COUNT(l)
        FROM Loan l
        GROUP BY l.status
    """)
    List<Object[]> countLoansByStatus();

    // ✅ Monthly loan trend (Line chart)
    @Query("""
        SELECT MONTH(l.appliedDate), COUNT(l)
        FROM Loan l
        GROUP BY MONTH(l.appliedDate)
        ORDER BY MONTH(l.appliedDate)
    """)
    List<Object[]> countLoansByMonth();

    // ✅ Total loan amount issued
    @Query("SELECT SUM(l.loanAmount) FROM Loan l")
    Double getTotalLoanAmount();

    // ✅ Average loan amount
    @Query("SELECT AVG(l.loanAmount) FROM Loan l")
    Double getAverageLoanAmount();
}
