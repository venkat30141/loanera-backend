package com.lao.loanmanagement.controller;

import com.lao.loanmanagement.dto.*;
import com.lao.loanmanagement.entity.*;
import com.lao.loanmanagement.repository.*;
import com.lao.loanmanagement.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/borrower")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BorrowerController {

    private final BorrowerRepository borrowerRepository;
    private final UserRepository userRepository;
    private final LoanService loanService;
    private final EmiScheduleRepository emiScheduleRepository;

    // =====================================================
    // ✅ 1️⃣ GET BORROWER PROFILE
    // =====================================================
    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getBorrowerProfile(@PathVariable Long userId) {

        return borrowerRepository.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // =====================================================
    // ✅ 2️⃣ CREATE BORROWER PROFILE
    // =====================================================
    @PostMapping("/profile")
    public ResponseEntity<?> createBorrowerProfile(
            @RequestBody BorrowerProfileRequestDto request) {

        // find user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // prevent duplicate profile
        if (borrowerRepository.existsByUserId(user.getId())) {
            return ResponseEntity.badRequest()
                    .body("Borrower profile already exists");
        }

        Borrower borrower = Borrower.builder()
                .user(user)
                .monthlyIncome(request.getMonthlyIncome())
                .creditScore(request.getCreditScore())
                .createdAt(LocalDateTime.now())
                .build();

        Borrower saved = borrowerRepository.save(borrower);

        return ResponseEntity.ok(saved);
    }

    // =====================================================
    // ✅ 3️⃣ EMI PREVIEW (BEFORE APPLYING LOAN)
    // =====================================================
    @PostMapping("/emi-preview")
    public ResponseEntity<EmiPreviewResponseDto> previewEmi(
            @RequestBody EmiPreviewRequestDto request) {

        double principal = request.getAmount();
        double annualRate = request.getInterestRate();
        int tenure = request.getTenureMonths();

        double monthlyRate = annualRate / 12 / 100;

        double emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, tenure))
                / (Math.pow(1 + monthlyRate, tenure) - 1);

        emi = Math.round(emi * 100.0) / 100.0;

        double totalPayable = Math.round(emi * tenure * 100.0) / 100.0;
        double totalInterest = Math.round((totalPayable - principal) * 100.0) / 100.0;

        EmiPreviewResponseDto response = new EmiPreviewResponseDto();
        response.setEmi(emi);
        response.setTotalPayable(totalPayable);
        response.setTotalInterest(totalInterest);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // ✅ 4️⃣ APPLY LOAN (ONLY IF PROFILE EXISTS)
    // =====================================================
    @PostMapping("/apply-loan")
    public ResponseEntity<?> applyLoan(
            @RequestBody LoanRequestDto request) {

        Borrower borrower = borrowerRepository.findById(request.getBorrowerId())
                .orElseThrow(() -> new RuntimeException("Borrower profile not found"));

        Loan loan = loanService.applyLoan(
                borrower.getId(),
                request.getAmount(),
                request.getInterestRate(),
                request.getTenureMonths()
        );

        return ResponseEntity.ok(loan);
    }

    // =====================================================
    // ✅ 5️⃣ GET ALL LOANS OF BORROWER (MY LOANS PAGE)
    // =====================================================
    @GetMapping("/my-loans/{userId}")
    public ResponseEntity<?> getMyLoans(@PathVariable Long userId) {

        // find borrower profile by userId
        Borrower borrower = borrowerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Borrower profile not found"));

        // get all loans of borrower
        return ResponseEntity.ok(
                loanService.getLoansByBorrower(borrower.getId())
        );
    }
    // =====================================================
// ✅ 5️⃣ VIEW EMI SCHEDULE OF A LOAN
// =====================================================
        @GetMapping("/emi/{loanId}")
        public ResponseEntity<?> getEmiSchedule(@PathVariable Long loanId) {

        return ResponseEntity.ok(
                 emiScheduleRepository.findByLoanId(loanId)
      );
    }

}
