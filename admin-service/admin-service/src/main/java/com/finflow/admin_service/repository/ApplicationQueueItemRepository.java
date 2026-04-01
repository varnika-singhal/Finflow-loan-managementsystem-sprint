package com.finflow.admin_service.repository;

import com.finflow.admin_service.entity.ApplicationQueueItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationQueueItemRepository extends JpaRepository<ApplicationQueueItem, Long> {

    Optional<ApplicationQueueItem> findByApplicationId(Long applicationId);

    List<ApplicationQueueItem> findAllByOrderByUpdatedAtDesc();
}
