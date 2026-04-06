package com.lao.loanmanagement.service;

import com.lao.loanmanagement.entity.*;
import com.lao.loanmanagement.entity.LoanStatus;

import java.util.List;

public interface LoanService {

    // =======================
    // BORROWER
    // =======================
    Loan applyLoan(Long borrowerId,
                   Double amount,
                   Double interestRate,
                   Integer tenureMonths);

    Payment payEmi(Long emiId);

    // 🔥 GET BORROWER LOANS
    List<Loan> getLoansByBorrower(Long borrowerId);

    // =======================
    // ADMIN
    // =======================
    List<Loan> getLoansByStatus(LoanStatus status);

    Loan approveLoan(Long loanId);

    Loan assignLender(Long loanId, Long lenderId);

    // =======================
    // LENDER
    // =======================
    Loan fundLoan(Long loanId);

    // 🔥 NEW — GET LOANS ASSIGNED TO LENDER
    List<Loan> getLoansByLender(Long lenderId);
}
