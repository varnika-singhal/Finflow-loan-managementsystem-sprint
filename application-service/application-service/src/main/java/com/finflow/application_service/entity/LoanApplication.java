package com.finflow.application_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User id is required")
    private Long userId;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Employment status is required")
    private String employmentStatus;

    @NotNull(message = "Annual income is required")
    @Positive(message = "Annual income must be positive")
    private Double annualIncome;

    @NotNull(message = "Loan amount is required")
    @Positive(message = "Loan amount must be positive")
    private Double loanAmount;

    @NotBlank(message = "Loan type is required")
    private String loanType;

    @NotNull(message = "Tenure is required")
    @Positive(message = "Tenure must be positive")
    private Integer tenure;

    private String status;
}
