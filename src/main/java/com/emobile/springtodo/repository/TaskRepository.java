package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.Task;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface TaskRepository {
    Task create(Task task);
    Task get(UUID taskId);
    void update(UUID taskId, Task task);
    void delete(UUID taskId);
    Page<Task> getAllPagedByAccountId(UUID accountId, int page, int size);
    void deleteAllByAccountId(UUID accountId);
}
