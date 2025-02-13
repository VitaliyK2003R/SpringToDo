package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.request.TaskRequest;
import com.emobile.springtodo.dto.request.UpdateTaskRequest;
import com.emobile.springtodo.dto.response.TaskResponse;
import com.emobile.springtodo.model.Account;
import com.emobile.springtodo.model.Task;
import com.emobile.springtodo.repository.TaskRepository;
import com.emobile.springtodo.util.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    @Override
    public TaskResponse create(UUID accountId, TaskRequest taskRequest) {
        Task task = taskMapper.toModel(taskRequest);
        task.setAccount(Account.builder().id(accountId).build());
        task = taskRepository.create(task);
        return taskMapper.toResponse(task);
    }

    @Override
    public TaskResponse get(UUID taskId) {
        return taskMapper.toResponse(taskRepository.get(taskId));
    }

    @Override
    public Page<TaskResponse> getAllPaged(UUID accountId, int page, int size) {
        return taskMapper.toPagedResponses(taskRepository.getAllPagedByAccountId(accountId, page, size));
    }

    @Override
    public void update(UUID taskId, UpdateTaskRequest updateTaskRequest) {
        taskRepository.update(taskId, taskMapper.toModel(updateTaskRequest));
    }

    @Override
    public void delete(UUID taskId) {
        taskRepository.delete(taskId);
    }

    @Override
    public List<Task> getAllByAccountId(UUID accountId) {
        return taskRepository.getAllPagedByAccountId(accountId, 0, 5).getContent();
    }

    @Override
    public void deleteAllByAccountId(UUID accountId) {
        taskRepository.deleteAllByAccountId(accountId);
    }
}
