package com.finflow.admin_service.service;

import com.finflow.admin_service.client.ApplicationServiceClient;
import com.finflow.admin_service.client.AuthServiceClient;
import com.finflow.admin_service.client.DocumentServiceClient;
import com.finflow.admin_service.dto.AdminDecisionRequest;
import com.finflow.admin_service.dto.DocumentResponse;
import com.finflow.admin_service.dto.UserUpdateRequest;
import com.finflow.admin_service.entity.ApplicationQueueItem;
import com.finflow.admin_service.entity.LoanDecision;
import com.finflow.admin_service.entity.Report;
import com.finflow.admin_service.messaging.AdminEventPublisher;
import com.finflow.admin_service.repository.ApplicationQueueItemRepository;
import com.finflow.admin_service.repository.LoanDecisionRepository;
import com.finflow.admin_service.repository.ReportRepository;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final LoanDecisionRepository repository;
    private final ReportRepository reportRepository;
    private final ApplicationQueueItemRepository applicationQueueItemRepository;
    private final ApplicationServiceClient applicationServiceClient;
    private final DocumentServiceClient documentServiceClient;
    private final AuthServiceClient authServiceClient;
    private final AdminEventPublisher adminEventPublisher;

    public AdminService(LoanDecisionRepository repository,
                        ReportRepository reportRepository,
                        ApplicationQueueItemRepository applicationQueueItemRepository,
                        ApplicationServiceClient applicationServiceClient,
                        DocumentServiceClient documentServiceClient,
                        AuthServiceClient authServiceClient,
                        AdminEventPublisher adminEventPublisher) {
        this.repository = repository;
        this.reportRepository = reportRepository;
        this.applicationQueueItemRepository = applicationQueueItemRepository;
        this.applicationServiceClient = applicationServiceClient;
        this.documentServiceClient = documentServiceClient;
        this.authServiceClient = authServiceClient;
        this.adminEventPublisher = adminEventPublisher;
    }

    // Save decision + update application status
    public LoanDecision makeDecision(LoanDecision decision, String authorizationHeader) {

        LoanDecision saved = repository.save(decision);
        adminEventPublisher.publishLoanDecisionMade(saved);
        return saved;
    }

    public LoanDecision makeDecision(Long applicationId,
                                     AdminDecisionRequest request,
                                     String authorizationHeader) {
        updateApplicationStatus(applicationId, "Under Review", authorizationHeader);

        LoanDecision decision = new LoanDecision();
        decision.setApplicationId(applicationId);
        decision.setDecision(request.getDecision());
        decision.setRemarks(request.getRemarks());
        return makeDecision(decision, authorizationHeader);
    }

    // Get decision by applicationId
    public LoanDecision getDecision(Long applicationId) {
        return repository.findTopByApplicationIdOrderByIdDesc(applicationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Decision not found"));
    }

    public List<LoanDecision> getAllDecisions() {
        return repository.findAll();
    }

    public LoanDecision getDecisionById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Decision not found"));
    }

    public List<LoanDecision> getDecisionsByStatus(String decision) {
        return repository.findByDecision(decision);
    }

    public void deleteDecision(Long id) {
        LoanDecision decision = getDecisionById(id);
        repository.delete(decision);
    }

    public List<ApplicationQueueItem> getApplicationQueue(String authorizationHeader) {
        return applicationQueueItemRepository.findAllByOrderByUpdatedAtDesc();
    }

    public DocumentResponse verifyDocument(Long id, String authorizationHeader) {
        return documentServiceClient.verifyDocument(id, authorizationHeader);
    }

    public List<?> getUsers(String authorizationHeader) {
        return authServiceClient.getUsers(authorizationHeader);
    }

    public Object updateUser(Long id, UserUpdateRequest request, String authorizationHeader) {
        if (request.getRole() != null && !request.getRole().isBlank()) {
            authServiceClient.updateUserRole(id, request.getRole(), authorizationHeader);
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            authServiceClient.updatePassword(id, request.getPassword(), authorizationHeader);
        }

        return authServiceClient.getUserById(id, authorizationHeader);
    }

    public Report generateReport(String authorizationHeader) {
        List<?> applications = getApplications(authorizationHeader);
        List<?> documents = getDocuments(authorizationHeader);
        List<?> users = getUsers(authorizationHeader);

        Report report = new Report();
        report.setGeneratedAt(LocalDateTime.now());
        report.setTotalApplications((long) applications.size());
        report.setDraftApplications(countByStatus(applications, "DRAFT"));
        report.setSubmittedApplications(countByStatus(applications, "SUBMITTED"));
        report.setDocsPendingApplications(countByStatus(applications, "DOCS PENDING"));
        report.setDocsVerifiedApplications(countByStatus(applications, "DOCS VERIFIED"));
        report.setUnderReviewApplications(countByStatus(applications, "UNDER REVIEW"));
        report.setApprovedApplications(countByStatus(applications, "APPROVED"));
        report.setRejectedApplications(countByStatus(applications, "REJECTED"));
        report.setClosedApplications(countByStatus(applications, "CLOSED"));
        report.setTotalDocuments((long) documents.size());
        report.setVerifiedDocuments(countByStatus(documents, "VERIFIED"));
        report.setRejectedDocuments(countByStatus(documents, "REJECTED"));
        report.setTotalUsers((long) users.size());

        return reportRepository.save(report);
    }

    private List<?> getApplications(String authorizationHeader) {
        List<Map<String, Object>> applications = applicationServiceClient.getApplications(authorizationHeader);
        return applications == null ? List.of() : applications;
    }

    private List<?> getDocuments(String authorizationHeader) {
        List<Map<String, Object>> documents = documentServiceClient.getDocuments(authorizationHeader);
        return documents == null ? List.of() : documents;
    }

    private long countByStatus(List<?> items, String expectedStatus) {
        long count = 0;
        for (Object item : items) {
            if (item instanceof LinkedHashMap<?, ?> map) {
                Object status = map.get("status");
                if (status != null && status.toString().trim().equalsIgnoreCase(expectedStatus)) {
                    count++;
                }
            }
        }
        return count;
    }

    private void updateApplicationStatus(Long applicationId, String status, String authorizationHeader) {
        try {
            applicationServiceClient.updateStatus(applicationId, status, authorizationHeader);
        } catch (FeignException exception) {
            throw new ResponseStatusException(
                    HttpStatus.valueOf(exception.status()),
                    exception.contentUTF8(),
                    exception
            );
        }
    }
}
