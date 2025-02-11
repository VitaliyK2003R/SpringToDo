package com.emobile.springtodo.controller;

import com.emobile.springtodo.api.AccountApi;
import com.emobile.springtodo.dto.request.AccountRequest;
import com.emobile.springtodo.dto.request.TaskRequest;
import com.emobile.springtodo.dto.request.UpdateTaskRequest;
import com.emobile.springtodo.dto.response.AccountResponse;
import com.emobile.springtodo.dto.response.TaskResponse;
import com.emobile.springtodo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountApi {
    private final AccountService accountService;

    @Override
    public ResponseEntity<AccountResponse> create(AccountRequest accountRequest) {
        AccountResponse accountResponse = accountService.create(accountRequest);
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AccountResponse> get(UUID accountId) {
        AccountResponse accountResponse = accountService.get(accountId);
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AccountResponse>> getAll() {
        List<AccountResponse> accountResponses = accountService.getAll();
        return new ResponseEntity<>(accountResponses, HttpStatus.OK);
    }

    @Override
    public void update(UUID accountId, AccountRequest accountRequest) throws AccountNotFoundException {
        accountService.update(accountId, accountRequest);
    }

    @Override
    public void delete(UUID accountId) {
        accountService.delete(accountId);
    }

    @Override
    public ResponseEntity<TaskResponse> createTask(UUID accountId, TaskRequest taskRequest) {
        TaskResponse taskResponse = accountService.createTask(accountId, taskRequest);
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TaskResponse> getTask(UUID accountId, UUID taskId) {
        TaskResponse taskResponse = accountService.getTask(accountId, taskId);
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Page<TaskResponse>> getAllTasks(UUID accountId, int page, int size) {
        Page<TaskResponse> taskResponses = accountService.getAllTasks(accountId, page, size);
        return new ResponseEntity<>(taskResponses, HttpStatus.OK);
    }

    @Override
    public void updateTask(UUID accountId, UUID taskId, UpdateTaskRequest taskRequest) {
        accountService.updateTask(accountId, taskId, taskRequest);
    }

    @Override
    public void deleteTask(UUID accountId, UUID taskId) {
        accountService.deleteTask(accountId, taskId);
    }
}
