package com.finflow.admin_service.dto;

import jakarta.validation.constraints.NotBlank;

public class AdminDecisionRequest {

    @NotBlank(message = "Decision is required")
    private String decision;

    private String remarks;

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
