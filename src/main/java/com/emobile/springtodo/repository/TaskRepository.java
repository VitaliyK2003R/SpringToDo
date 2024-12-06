package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.Task;

import java.util.List;
import java.util.UUID;

public interface TaskRepository {
    Task create(Task task);
    Task get(UUID taskId);
    void update(UUID taskId, Task task);
    void delete(UUID taskId);
    List<Task> getAllPagedByAccountId(UUID accountId, int page, int size);
    void deleteAllByAccountId(UUID accountId);
}
