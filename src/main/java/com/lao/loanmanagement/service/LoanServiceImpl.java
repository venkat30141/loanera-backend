package com.lao.loanmanagement.service;

import com.lao.loanmanagement.entity.*;
import com.lao.loanmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BorrowerRepository borrowerRepository;
    private final LenderRepository lenderRepository;
    private final EmiScheduleRepository emiScheduleRepository;
    private final PaymentRepository paymentRepository;

    // =======================
    // BORROWER: Apply Loan
    // =======================
    @Override
    public Loan applyLoan(Long borrowerId,
                          Double amount,
                          Double interestRate,
                          Integer tenureMonths) {

        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));

        Loan loan = Loan.builder()
                .borrower(borrower)
                .loanAmount(amount)
                .interestRate(interestRate)
                .tenureMonths(tenureMonths)
                .status(LoanStatus.APPLIED)
                
                .build();

        return loanRepository.save(loan);
    }

    // =======================
    // 🔥 BORROWER: GET MY LOANS
    // =======================
    @Override
    public List<Loan> getLoansByBorrower(Long borrowerId) {
        return loanRepository.findByBorrowerId(borrowerId);
    }

    // =======================
    // ADMIN: View loans by status
    // =======================
    @Override
    public List<Loan> getLoansByStatus(LoanStatus status) {
        return loanRepository.findByStatus(status);
    }

    // =======================
    // ADMIN: Approve Loan
    // =======================
    @Override
    public Loan approveLoan(Long loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() != LoanStatus.APPLIED) {
            throw new RuntimeException("Only APPLIED loans can be approved");
        }

        loan.setStatus(LoanStatus.APPROVED);
        loan.setApprovedDate(LocalDateTime.now());

        return loanRepository.save(loan);
    }

    // =======================
    // ADMIN: Assign Lender
    // =======================
    @Override
    public Loan assignLender(Long loanId, Long lenderId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() != LoanStatus.APPROVED) {
            throw new RuntimeException("Loan must be APPROVED before assigning lender");
        }

        Lender lender = lenderRepository.findById(lenderId)
                .orElseThrow(() -> new RuntimeException("Lender not found"));

        loan.setLender(lender);

        return loanRepository.save(loan);
    }

    // =======================
    // 🔥 LENDER: GET ASSIGNED LOANS
    // =======================
    @Override
    public List<Loan> getLoansByLender(Long lenderId) {

        return loanRepository.findByLenderIdAndStatusIn(
                lenderId,
                List.of(LoanStatus.APPROVED, LoanStatus.ACTIVE)
        );
    }

    // =======================
    // LENDER: Fund Loan + Generate EMI
    // =======================
    @Override
    public Loan fundLoan(Long loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() != LoanStatus.APPROVED) {
            throw new RuntimeException("Loan must be APPROVED before funding");
        }

        Lender lender = loan.getLender();
        if (lender == null) {
            throw new RuntimeException("Lender not assigned to loan");
        }

        if (lender.getAvailableFunds() < loan.getLoanAmount()) {
            throw new RuntimeException("Insufficient lender funds");
        }

        // Deduct lender funds
        lender.setAvailableFunds(
                lender.getAvailableFunds() - loan.getLoanAmount()
        );
        lenderRepository.save(lender);

        // Activate loan
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setFundedDate(LocalDateTime.now());
        loanRepository.save(loan);

        // Generate EMI schedule
        generateEmiSchedule(loan);

        return loan;
    }

    // =======================
    // EMI GENERATION LOGIC
    // =======================
    private void generateEmiSchedule(Loan loan) {

        double principal = loan.getLoanAmount();
        double annualRate = loan.getInterestRate();
        int tenureMonths = loan.getTenureMonths();

        double monthlyRate = annualRate / 12 / 100;

        double emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, tenureMonths))
                / (Math.pow(1 + monthlyRate, tenureMonths) - 1);

        emi = Math.round(emi * 100.0) / 100.0;

        for (int i = 1; i <= tenureMonths; i++) {

            EmiSchedule emiSchedule = EmiSchedule.builder()
                    .loan(loan)
                    .emiAmount(emi)
                    .dueDate(LocalDate.now().plusMonths(i))
                    .isPaid(false)
                    .build();

            emiScheduleRepository.save(emiSchedule);
        }
    }

    // =======================
    // BORROWER: PAY EMI
    // =======================
    @Override
    public Payment payEmi(Long emiId) {

        EmiSchedule emi = emiScheduleRepository.findById(emiId)
                .orElseThrow(() -> new RuntimeException("EMI not found"));

        if (emi.getIsPaid()) {
            throw new RuntimeException("EMI already paid");
        }

        // Create payment record
        Payment payment = Payment.builder()
                .loan(emi.getLoan())
                .amountPaid(emi.getEmiAmount())
                .paymentDate(LocalDateTime.now())
                .status(PaymentStatus.SUCCESS)
                .build();

        paymentRepository.save(payment);

        // Mark EMI as paid
        emi.setIsPaid(true);
        emiScheduleRepository.save(emi);

        // 🔥 AUTO-CLOSE LOAN IF LAST EMI PAID
        checkAndCloseLoan(emi.getLoan());

        return payment;
    }

    // =======================
    // AUTO-CLOSE LOAN LOGIC
    // =======================
    private void checkAndCloseLoan(Loan loan) {

        boolean hasUnpaidEmi =
                emiScheduleRepository.findByLoanId(loan.getId())
                        .stream()
                        .anyMatch(e -> !e.getIsPaid());

        if (!hasUnpaidEmi) {
            loan.setStatus(LoanStatus.CLOSED);
            loan.setClosedDate(LocalDateTime.now());
            loanRepository.save(loan);
        }
    }
}
