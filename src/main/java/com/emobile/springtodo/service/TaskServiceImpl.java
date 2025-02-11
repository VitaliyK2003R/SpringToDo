package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.request.TaskRequest;
import com.emobile.springtodo.dto.request.UpdateTaskRequest;
import com.emobile.springtodo.dto.response.TaskResponse;
import com.emobile.springtodo.exception.TaskNotFoundException;
import com.emobile.springtodo.model.Account;
import com.emobile.springtodo.model.Task;
import com.emobile.springtodo.repository.TaskRepository;
import com.emobile.springtodo.util.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    @Override
    public TaskResponse create(UUID accountId, TaskRequest taskRequest) {
        Task creatableTask = taskMapper.toModel(taskRequest);
        Account account = Account.builder().id(accountId).build();
        creatableTask.setAccount(account);
        creatableTask = taskRepository.save(creatableTask);
        return taskMapper.toResponse(creatableTask);
    }

    @Override
    public TaskResponse get(UUID taskId) {
        return taskMapper.toResponse(taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new));
    }

    @Override
    public Page<TaskResponse> getAllPaged(UUID accountId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskMapper.toPagedResponses(taskRepository.findAllByAccount_Id(accountId, pageable));
    }

    @Override
    public void update(UUID taskId, UpdateTaskRequest updateTaskRequest) {
        Task updatedTask = taskMapper.toModel(updateTaskRequest);
        Task updatableTask = getTask(taskId);
        updatableTask.setAccount(updatedTask.getAccount());
        updatableTask.setName(updatedTask.getName());
        updatableTask.setTitle(updatedTask.getTitle());
        updatableTask.setStart(updatedTask.getStart());
        updatableTask.setFinish(updatedTask.getFinish());
        taskRepository.save(updatableTask);
    }

    @Override
    public void delete(UUID taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    public List<Task> getAll(UUID accountId) {
        return taskRepository.findAllByAccount_Id(accountId);
    }

    @Override
    public void deleteAllByAccountId(UUID accountId) {
        taskRepository.deleteAllByAccountId(accountId);
    }

    private Task getTask(UUID taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isEmpty()) {
            throw new TaskNotFoundException();
        }
        return optionalTask.get();
    }
}
