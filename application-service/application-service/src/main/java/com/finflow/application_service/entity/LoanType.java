package com.finflow.application_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Loan type name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Minimum amount is required")
    @Positive(message = "Minimum amount must be positive")
    private Double minAmount;

    @NotNull(message = "Maximum amount is required")
    @Positive(message = "Maximum amount must be positive")
    private Double maxAmount;

    @NotNull(message = "Minimum tenure is required")
    @Positive(message = "Minimum tenure must be positive")
    private Integer minTenure;

    @NotNull(message = "Maximum tenure is required")
    @Positive(message = "Maximum tenure must be positive")
    private Integer maxTenure;

    private boolean active;
}
