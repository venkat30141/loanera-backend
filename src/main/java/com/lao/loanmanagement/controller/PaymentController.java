package com.lao.loanmanagement.controller;

import com.lao.loanmanagement.entity.Payment;
import com.lao.loanmanagement.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    private final LoanService loanService;

    @PutMapping("/emi/{emiId}/pay")
    public ResponseEntity<Payment> payEmi(@PathVariable Long emiId) {
        return ResponseEntity.ok(
                loanService.payEmi(emiId)
        );
    }
}
