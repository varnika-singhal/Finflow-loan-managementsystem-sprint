package com.finflow.admin_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime generatedAt;
    private Long totalApplications;
    private Long draftApplications;
    private Long submittedApplications;
    private Long docsPendingApplications;
    private Long docsVerifiedApplications;
    private Long underReviewApplications;
    private Long approvedApplications;
    private Long rejectedApplications;
    private Long closedApplications;
    private Long totalDocuments;
    private Long verifiedDocuments;
    private Long rejectedDocuments;
    private Long totalUsers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public Long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(Long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public Long getDraftApplications() {
        return draftApplications;
    }

    public void setDraftApplications(Long draftApplications) {
        this.draftApplications = draftApplications;
    }

    public Long getSubmittedApplications() {
        return submittedApplications;
    }

    public void setSubmittedApplications(Long submittedApplications) {
        this.submittedApplications = submittedApplications;
    }

    public Long getDocsPendingApplications() {
        return docsPendingApplications;
    }

    public void setDocsPendingApplications(Long docsPendingApplications) {
        this.docsPendingApplications = docsPendingApplications;
    }

    public Long getDocsVerifiedApplications() {
        return docsVerifiedApplications;
    }

    public void setDocsVerifiedApplications(Long docsVerifiedApplications) {
        this.docsVerifiedApplications = docsVerifiedApplications;
    }

    public Long getUnderReviewApplications() {
        return underReviewApplications;
    }

    public void setUnderReviewApplications(Long underReviewApplications) {
        this.underReviewApplications = underReviewApplications;
    }

    public Long getApprovedApplications() {
        return approvedApplications;
    }

    public void setApprovedApplications(Long approvedApplications) {
        this.approvedApplications = approvedApplications;
    }

    public Long getRejectedApplications() {
        return rejectedApplications;
    }

    public void setRejectedApplications(Long rejectedApplications) {
        this.rejectedApplications = rejectedApplications;
    }

    public Long getClosedApplications() {
        return closedApplications;
    }

    public void setClosedApplications(Long closedApplications) {
        this.closedApplications = closedApplications;
    }

    public Long getTotalDocuments() {
        return totalDocuments;
    }

    public void setTotalDocuments(Long totalDocuments) {
        this.totalDocuments = totalDocuments;
    }

    public Long getVerifiedDocuments() {
        return verifiedDocuments;
    }

    public void setVerifiedDocuments(Long verifiedDocuments) {
        this.verifiedDocuments = verifiedDocuments;
    }

    public Long getRejectedDocuments() {
        return rejectedDocuments;
    }

    public void setRejectedDocuments(Long rejectedDocuments) {
        this.rejectedDocuments = rejectedDocuments;
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }
}
