package com.finflow.document_service.service;

import com.finflow.document_service.entity.Document;
import com.finflow.document_service.messaging.DocumentEventPublisher;
import com.finflow.document_service.repository.DocumentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
public class DocumentService {

    private final DocumentRepository repository;
    private final DocumentEventPublisher documentEventPublisher;

    public DocumentService(DocumentRepository repository, DocumentEventPublisher documentEventPublisher) {
        this.repository = repository;
        this.documentEventPublisher = documentEventPublisher;
    }

    // Upload document
    public Document upload(Document doc) {
        doc.setStatus("Uploaded");
        Document saved = repository.save(doc);
        documentEventPublisher.publishDocumentStatusChanged(saved);
        return saved;
    }

    public List<Document> getAllDocuments() {
        return repository.findAll();
    }

    // Get documents by applicationId
    public List<Document> getDocuments(Long applicationId) {
        return repository.findByApplicationId(applicationId);
    }

    public Document getDocumentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
    }

    public List<Document> getDocumentsByStatus(String status) {
        return repository.findByStatus(status);
    }

    // Verify document
    public Document verify(Long id) {
        Document doc = getDocumentById(id);

        doc.setStatus("Verified");
        Document saved = repository.save(doc);
        documentEventPublisher.publishDocumentStatusChanged(saved);
        return saved;
    }

    public Document updateDocument(Long id, Document updatedDocument) {
        Document doc = getDocumentById(id);
        doc.setDocumentName(updatedDocument.getDocumentName());
        doc.setDocumentType(updatedDocument.getDocumentType());
        doc.setApplicationId(updatedDocument.getApplicationId());
        doc.setStatus("Uploaded");
        Document saved = repository.save(doc);
        documentEventPublisher.publishDocumentStatusChanged(saved);
        return saved;
    }

    public Document replaceDocument(Long id, Document updatedDocument) {
        return updateDocument(id, updatedDocument);
    }

    public Document rejectDocument(Long id) {
        Document doc = getDocumentById(id);
        doc.setStatus("Rejected");
        Document saved = repository.save(doc);
        documentEventPublisher.publishDocumentStatusChanged(saved);
        return saved;
    }

    public void deleteDocument(Long id) {
        Document doc = getDocumentById(id);
        repository.delete(doc);
    }
}
