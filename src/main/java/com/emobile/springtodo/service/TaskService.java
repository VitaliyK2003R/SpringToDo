package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.request.TaskRequest;
import com.emobile.springtodo.dto.request.UpdateTaskRequest;
import com.emobile.springtodo.dto.response.TaskResponse;
import com.emobile.springtodo.model.Task;

import java.util.List;
import java.util.UUID;

/**
 * Service for working with tasks.
 *
 * @author Vitaliy
 */
public interface TaskService {
    TaskResponse create(UUID accountId, TaskRequest taskRequest);

    TaskResponse get(UUID taskId);

    List<TaskResponse> getAllPaged(UUID accountId, int page, int size);

    void update(UUID taskId, UpdateTaskRequest updateTaskRequest);

    void delete(UUID taskId);

    List<Task> getAllByAccountId(UUID accountId);

    void deleteAllByAccountId(UUID accountId);
}
