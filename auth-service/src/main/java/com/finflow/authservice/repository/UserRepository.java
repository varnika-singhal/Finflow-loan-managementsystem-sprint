package com.finflow.authservice.repository;

import com.finflow.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    List<User> findByRole(String role);

    Optional<User> findTopByUsernameOrderByIdDesc(String username);
}
