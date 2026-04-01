package com.finflow.document_service.service;

import com.finflow.document_service.entity.Document;
import com.finflow.document_service.messaging.DocumentEventPublisher;
import com.finflow.document_service.repository.DocumentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository repository;

    @Mock
    private DocumentEventPublisher documentEventPublisher;

    @InjectMocks
    private DocumentService documentService;

    @Test
    void uploadShouldSetStatusToUploaded() {
        Document document = buildDocument();
        when(repository.save(any(Document.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Document saved = documentService.upload(document);

        assertEquals("Uploaded", saved.getStatus());
        org.mockito.Mockito.verify(documentEventPublisher).publishDocumentStatusChanged(saved);
    }

    @Test
    void verifyShouldSetStatusToVerified() {
        Document document = buildDocument();
        document.setId(11L);
        document.setStatus("Uploaded");

        when(repository.findById(11L)).thenReturn(Optional.of(document));
        when(repository.save(any(Document.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Document verified = documentService.verify(11L);

        assertEquals("Verified", verified.getStatus());
        org.mockito.Mockito.verify(documentEventPublisher).publishDocumentStatusChanged(verified);
    }

    @Test
    void getDocumentByIdShouldThrowNotFoundWhenDocumentIsMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> documentService.getDocumentById(99L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Document not found", exception.getReason());
    }

    private Document buildDocument() {
        Document document = new Document();
        document.setApplicationId(5L);
        document.setDocumentName("Aadhar Card");
        document.setDocumentType("Identity Proof");
        return document;
    }
}
