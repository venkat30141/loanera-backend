package com.lao.loanmanagement.repository;

import com.lao.loanmanagement.entity.Lender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LenderRepository extends JpaRepository<Lender, Long> {

    Optional<Lender> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
