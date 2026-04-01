package com.finflow.admin_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "auth-service", path = "/auth/users")
public interface AuthServiceClient {

    @GetMapping
    List<Object> getUsers(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader);

    @PutMapping("/{id}/role")
    Object updateUserRole(
            @PathVariable("id") Long id,
            @RequestParam("role") String role,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    );

    @PutMapping("/{id}/password")
    Object updatePassword(
            @PathVariable("id") Long id,
            @RequestParam("password") String password,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    );

    @GetMapping("/{id}")
    Object getUserById(
            @PathVariable("id") Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    );
}
