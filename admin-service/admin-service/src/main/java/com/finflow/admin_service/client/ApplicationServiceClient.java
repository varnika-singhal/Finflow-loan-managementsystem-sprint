package com.finflow.admin_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "application-service", path = "/applications")
public interface ApplicationServiceClient {

    @GetMapping
    List<Map<String, Object>> getApplications(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    );

    @PutMapping("/{id}/status")
    void updateStatus(
            @PathVariable("id") Long applicationId,
            @RequestParam("status") String status,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    );
}
