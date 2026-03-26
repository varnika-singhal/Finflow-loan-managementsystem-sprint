package com.finflow.document_service.service;

import com.finflow.document_service.entity.Document;
import com.finflow.document_service.repository.DocumentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    // Upload document
    public Document upload(Document doc) {
        doc.setStatus("Uploaded");
        return repository.save(doc);
    }

    // Get documents by applicationId
    public List<Document> getDocuments(Long applicationId) {
        return repository.findByApplicationId(applicationId);
    }

    // Verify document
    public Document verify(Long id) {
        Document doc = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));

        doc.setStatus("Verified");
        return repository.save(doc);
    }
}
