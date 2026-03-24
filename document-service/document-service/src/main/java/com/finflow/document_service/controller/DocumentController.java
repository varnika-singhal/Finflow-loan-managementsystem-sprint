package com.finflow.document_service.controller;

import com.finflow.document_service.entity.Document;
import com.finflow.document_service.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService service;

    // Upload document
    @PostMapping("/upload")
    public Document upload(@RequestBody Document doc) {
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