package com.finflow.document_service.service;

import com.finflow.document_service.entity.Document;
import com.finflow.document_service.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository repository;

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
                .orElseThrow(() -> new RuntimeException("Document not found"));

        doc.setStatus("Verified");
        return repository.save(doc);
    }
}