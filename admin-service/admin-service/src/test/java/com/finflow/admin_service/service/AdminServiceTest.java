package com.finflow.admin_service.service;

import com.finflow.admin_service.client.ApplicationServiceClient;
import com.finflow.admin_service.client.AuthServiceClient;
import com.finflow.admin_service.client.DocumentServiceClient;
import com.finflow.admin_service.entity.ApplicationQueueItem;
import com.finflow.admin_service.entity.LoanDecision;
import com.finflow.admin_service.entity.Report;
import com.finflow.admin_service.messaging.AdminEventPublisher;
import com.finflow.admin_service.repository.ApplicationQueueItemRepository;
import com.finflow.admin_service.repository.LoanDecisionRepository;
import com.finflow.admin_service.repository.ReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private LoanDecisionRepository repository;

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private ApplicationQueueItemRepository applicationQueueItemRepository;

    @Mock
    private ApplicationServiceClient applicationServiceClient;

    @Mock
    private DocumentServiceClient documentServiceClient;

    @Mock
    private AuthServiceClient authServiceClient;

    @Mock
    private AdminEventPublisher adminEventPublisher;

    @InjectMocks
    private AdminService adminService;

    @Test
    void getApplicationQueueShouldReturnStoredInboxItemsOrderedByMostRecent() {
        ApplicationQueueItem queueItem = new ApplicationQueueItem();
        queueItem.setApplicationId(10L);
        queueItem.setCurrentStatus("Submitted");
        when(applicationQueueItemRepository.findAllByOrderByUpdatedAtDesc()).thenReturn(List.of(queueItem));

        List<ApplicationQueueItem> queue = adminService.getApplicationQueue("Bearer test-token");

        assertEquals(1, queue.size());
        assertEquals(10L, queue.get(0).getApplicationId());
    }

    @Test
    void generateReportShouldCountApplicationsDocumentsAndUsersCorrectly() {
        when(applicationServiceClient.getApplications(anyString())).thenReturn(List.of(
                application("Draft"),
                application("Approved"),
                application("Rejected")
        ));

        when(documentServiceClient.getDocuments(anyString())).thenReturn(List.of(
                document("Verified"),
                document("Rejected")
        ));

        when(authServiceClient.getUsers(anyString())).thenReturn(List.of(
                new Object(),
                new Object(),
                new Object()
        ));

        when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Report report = adminService.generateReport("Bearer admin-token");

        assertEquals(3L, report.getTotalApplications());
        assertEquals(1L, report.getDraftApplications());
        assertEquals(1L, report.getApprovedApplications());
        assertEquals(1L, report.getRejectedApplications());
        assertEquals(2L, report.getTotalDocuments());
        assertEquals(1L, report.getVerifiedDocuments());
        assertEquals(1L, report.getRejectedDocuments());
        assertEquals(3L, report.getTotalUsers());
        verify(reportRepository).save(any(Report.class));
    }

    @Test
    void makeDecisionShouldSaveDecisionAndCallApplicationServiceStatusUpdate() {
        LoanDecision decision = new LoanDecision();
        decision.setApplicationId(5L);
        decision.setDecision("Approved");
        decision.setRemarks("Eligible applicant");

        when(repository.save(decision)).thenReturn(decision);

        LoanDecision saved = adminService.makeDecision(decision, "Bearer admin-token");

        assertEquals(5L, saved.getApplicationId());
        assertEquals("Approved", saved.getDecision());
        verify(repository).save(decision);
        verify(adminEventPublisher).publishLoanDecisionMade(decision);
    }

    private LinkedHashMap<String, Object> application(String status) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("status", status);
        return map;
    }

    private LinkedHashMap<String, Object> document(String status) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("status", status);
        return map;
    }
}
