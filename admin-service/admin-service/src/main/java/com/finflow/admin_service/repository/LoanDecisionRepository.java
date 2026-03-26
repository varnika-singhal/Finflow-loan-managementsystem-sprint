package com.finflow.admin_service.repository;

import com.finflow.admin_service.entity.LoanDecision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanDecisionRepository extends JpaRepository<LoanDecision, Long> {

    Optional<LoanDecision> findTopByApplicationIdOrderByIdDesc(Long applicationId);
}
