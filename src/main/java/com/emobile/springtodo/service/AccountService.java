package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.request.AccountRequest;
import com.emobile.springtodo.dto.request.TaskRequest;
import com.emobile.springtodo.dto.request.UpdateTaskRequest;
import com.emobile.springtodo.dto.response.AccountResponse;
import com.emobile.springtodo.dto.response.TaskResponse;
import org.springframework.data.domain.Page;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountResponse create(AccountRequest accountRequest);
    AccountResponse get(UUID accountId);
    List<AccountResponse> getAll();
    void update(UUID accountId, AccountRequest accountRequest) throws AccountNotFoundException;
    void delete(UUID accountId);
    TaskResponse createTask(UUID accountId, TaskRequest taskRequest);
    TaskResponse getTask(UUID accountId, UUID taskId);
    Page<TaskResponse> getAllTasks(UUID accountId, int page, int size);
    void updateTask(UUID accountId, UUID taskId, UpdateTaskRequest taskRequest);
    void deleteTask(UUID accountId, UUID taskId);
}
