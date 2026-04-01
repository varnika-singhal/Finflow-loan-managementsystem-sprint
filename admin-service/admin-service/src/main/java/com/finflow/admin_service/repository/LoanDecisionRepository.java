package com.finflow.admin_service.repository;

import com.finflow.admin_service.entity.LoanDecision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanDecisionRepository extends JpaRepository<LoanDecision, Long> {
    List<LoanDecision> findByDecision(String decision);

    Optional<LoanDecision> findTopByApplicationIdOrderByIdDesc(Long applicationId);
}
