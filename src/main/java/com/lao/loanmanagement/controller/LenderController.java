package com.lao.loanmanagement.controller;

import com.lao.loanmanagement.dto.LenderProfileRequestDto;
import com.lao.loanmanagement.entity.Lender;
import com.lao.loanmanagement.entity.Loan;
import com.lao.loanmanagement.entity.User;
import com.lao.loanmanagement.repository.LenderRepository;
import com.lao.loanmanagement.repository.UserRepository;
import com.lao.loanmanagement.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/lender")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class LenderController {

    private final LoanService loanService;
    private final LenderRepository lenderRepository;
    private final UserRepository userRepository;

    // =====================================================
    // 1️⃣ GET LENDER PROFILE
    // =====================================================
    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getLenderProfile(@PathVariable Long userId) {

        return lenderRepository.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // =====================================================
    // 2️⃣ CREATE LENDER PROFILE
    // =====================================================
    @PostMapping("/profile")
    public ResponseEntity<?> createLenderProfile(
            @RequestBody LenderProfileRequestDto request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // prevent duplicate profile
        if (lenderRepository.existsByUserId(user.getId())) {
            return ResponseEntity.badRequest()
                    .body("Lender profile already exists");
        }

        Lender lender = Lender.builder()
                .user(user)
                .availableFunds(request.getAvailableFunds())
                .createdAt(LocalDateTime.now())
                .build();

        Lender saved = lenderRepository.save(lender);

        return ResponseEntity.ok(saved);
    }

    // =====================================================
    // 3️⃣ GET LOANS ASSIGNED TO LENDER (APPROVED + ACTIVE)
    // =====================================================
    @GetMapping("/my-loans/{userId}")
    public ResponseEntity<?> getAssignedLoans(@PathVariable Long userId) {

        // find lender profile
        Lender lender = lenderRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Lender profile not found"));

        List<Loan> loans = loanService.getLoansByLender(lender.getId());

        return ResponseEntity.ok(loans);
    }

    // =====================================================
    // 4️⃣ FUND LOAN
    // =====================================================
    @PutMapping("/loan/{loanId}/fund")
    public ResponseEntity<Loan> fundLoan(@PathVariable Long loanId) {

        return ResponseEntity.ok(
                loanService.fundLoan(loanId)
        );
    }
}
