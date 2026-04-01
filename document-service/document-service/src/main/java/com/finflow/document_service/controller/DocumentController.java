package com.finflow.document_service.controller;

import com.finflow.document_service.entity.Document;
import com.finflow.document_service.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    // Upload document
    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public Document upload(@Valid @RequestBody Document doc) {
        return service.upload(doc);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Document> getAllDocuments() {
        return service.getAllDocuments();
    }

    // Get documents by applicationId
    @GetMapping("/{applicationId}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public List<Document> getDocuments(@PathVariable Long applicationId) {
        return service.getDocuments(applicationId);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public Document getDocumentById(@PathVariable Long id) {
        return service.getDocumentById(id);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Document> getDocumentsByStatus(@PathVariable String status) {
        return service.getDocumentsByStatus(status);
    }

    // Verify document (admin)
    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public Document verify(@PathVariable Long id) {
        return service.verify(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public Document updateDocument(@PathVariable Long id, @Valid @RequestBody Document doc) {
        return service.updateDocument(id, doc);
    }

    @PutMapping("/{id}/replace")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public Document replaceDocument(@PathVariable Long id, @Valid @RequestBody Document doc) {
        return service.replaceDocument(id, doc);
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public Document rejectDocument(@PathVariable Long id) {
        return service.rejectDocument(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> deleteDocument(@PathVariable Long id) {
        service.deleteDocument(id);
        return Map.of("message", "Document deleted");
    }

}
