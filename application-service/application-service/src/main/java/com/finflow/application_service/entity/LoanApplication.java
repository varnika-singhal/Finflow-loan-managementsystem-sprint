package com.finflow.application_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String fullName;
    private String employmentStatus;
    private Double annualIncome;

    private Double loanAmount;
    private Integer tenure;

    private String status;
}
