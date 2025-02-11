package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    Page<Task> findAllByAccount_Id(UUID accountId, Pageable pageable);
    List<Task> findAllByAccount_Id(UUID accountId);
    void deleteAllByAccountId(UUID accountId);
}
