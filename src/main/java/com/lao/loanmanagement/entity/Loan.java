package com.lao.loanmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================
    // RELATIONSHIPS
    // =====================
    @ManyToOne
    @JoinColumn(name = "borrower_id", nullable = false)
    private Borrower borrower;

    @ManyToOne
    @JoinColumn(name = "lender_id")
    private Lender lender;

    // =====================
    // LOAN DETAILS
    // =====================
    @Column(nullable = false)
    private Double loanAmount;

    @Column(nullable = false)
    private Double interestRate;

    @Column(nullable = false)
    private Integer tenureMonths;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;

    // =====================
    // DATE TRACKING (IMPORTANT)
    // =====================

    // ✅ Automatically set when loan is CREATED
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime appliedDate;

    // ✅ Set when admin APPROVES loan
    private LocalDateTime approvedDate;

    // ✅ Set when lender FUNDS loan
    private LocalDateTime fundedDate;

    // ✅ Set when loan is FULLY PAID
    private LocalDateTime closedDate;
}
