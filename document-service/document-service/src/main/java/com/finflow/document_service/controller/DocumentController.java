package com.finflow.document_service.controller;

import com.finflow.document_service.entity.Document;
import com.finflow.document_service.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    // Upload document
    @PostMapping("/upload")
    public Document upload(@Valid @RequestBody Document doc) {
        return service.upload(doc);
    }

    // Get documents by applicationId
    @GetMapping("/{applicationId}")
    public List<Document> getDocuments(@PathVariable Long applicationId) {
        return service.getDocuments(applicationId);
    }

    // Verify document (admin)
    @PutMapping("/{id}/verify")
    public Document verify(@PathVariable Long id) {
        return service.verify(id);
    }
}
