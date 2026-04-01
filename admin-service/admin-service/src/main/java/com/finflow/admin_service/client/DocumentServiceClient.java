package com.finflow.admin_service.client;

import com.finflow.admin_service.dto.DocumentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;

@FeignClient(name = "document-service", path = "/documents")
public interface DocumentServiceClient {

    @GetMapping
    List<Map<String, Object>> getDocuments(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    );

    @PutMapping("/{id}/verify")
    DocumentResponse verifyDocument(
            @PathVariable("id") Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    );
}
