package com.lao.loanmanagement.repository;

import com.lao.loanmanagement.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

    Optional<Borrower> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
