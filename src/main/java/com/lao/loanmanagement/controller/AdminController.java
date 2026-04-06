package com.lao.loanmanagement.controller;

import com.lao.loanmanagement.dto.CreateUserRequest;
import com.lao.loanmanagement.dto.UpdateUserRequest;
import com.lao.loanmanagement.entity.Loan;
import com.lao.loanmanagement.entity.LoanStatus;
import com.lao.loanmanagement.entity.User;
import com.lao.loanmanagement.service.LoanService;
import com.lao.loanmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    private final LoanService loanService;
    private final UserService userService;

    /* =========================
       LOAN MANAGEMENT
    ========================== */

    // 1️⃣ APPLIED LOANS
    @GetMapping("/loans/applied")
    public ResponseEntity<List<Loan>> getAppliedLoans() {
        return ResponseEntity.ok(
                loanService.getLoansByStatus(LoanStatus.APPLIED)
        );
    }

    // 2️⃣ APPROVED LOANS
    @GetMapping("/loans/approved")
    public ResponseEntity<List<Loan>> getApprovedLoans() {
        return ResponseEntity.ok(
                loanService.getLoansByStatus(LoanStatus.APPROVED)
        );
    }

    // 3️⃣ APPROVE LOAN
    @PostMapping("/approve/{loanId}")
    public ResponseEntity<Loan> approveLoan(@PathVariable Long loanId) {
        return ResponseEntity.ok(
                loanService.approveLoan(loanId)
        );
    }

    // 4️⃣ ASSIGN LENDER
    @PostMapping("/assign-lender/{loanId}/{lenderId}")
    public ResponseEntity<Loan> assignLender(
            @PathVariable Long loanId,
            @PathVariable Long lenderId
    ) {
        return ResponseEntity.ok(
                loanService.assignLender(loanId, lenderId)
        );
    }

    /* =========================
       USER MANAGEMENT (ADMIN CRUD)
    ========================== */

    // 5️⃣ VIEW ALL USERS
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    // 6️⃣ CREATE USER
    @PostMapping("/users")
    public ResponseEntity<User> createUser(
            @RequestBody CreateUserRequest request
    ) {
        return ResponseEntity.ok(
                userService.createUser(
                        request.getName(),
                        request.getEmail(),
                        request.getPassword(),
                        request.getRole()
                )
        );
    }

    // 7️⃣ UPDATE USER
    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long userId,
            @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity.ok(
                userService.updateUser(
                        userId,
                        request.getName(),
                        request.getEmail()
                        //request.getCreditScore()
                )
        );
    }

    // 8️⃣ DELETE USER
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
