package com.finflow.application_service.repository;

import com.finflow.application_service.entity.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanTypeRepository extends JpaRepository<LoanType, Long> {

    List<LoanType> findByActiveTrue();

    Optional<LoanType> findByNameIgnoreCase(String name);
}
