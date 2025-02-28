package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.request.AccountRequest;
import com.emobile.springtodo.dto.request.TaskRequest;
import com.emobile.springtodo.dto.request.UpdateTaskRequest;
import com.emobile.springtodo.dto.response.AccountResponse;
import com.emobile.springtodo.dto.response.TaskResponse;
import com.emobile.springtodo.model.Account;
import com.emobile.springtodo.model.Task;
import com.emobile.springtodo.repository.AccountRepository;
import com.emobile.springtodo.util.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountMapper accountMapper;
    private final TaskService taskService;
    private final AccountRepository accountRepository;

    @CachePut(value = "accounts", key = "#result.id()")
    @Override
    public AccountResponse create(AccountRequest accountRequest) {
        Account creatableAccount = accountMapper.toModel(accountRequest);
        creatableAccount = accountRepository.create(creatableAccount);
        return accountMapper.toResponse(creatableAccount);
    }

    @Cacheable(value = "accounts", key = "#accountId")
    @Override
    public AccountResponse get(UUID accountId) {
        Account account = accountRepository.get(accountId);
        List<Task> accountTasks = taskService.getAllByAccountId(accountId);
        account.setTasks(accountTasks);
        return accountMapper.toResponse(account);
    }

    @Override
    public List<AccountResponse> getAll() {
        List<Account> accounts = accountRepository.getAll();
        for (Account account : accounts) {
            List<Task> accountTasks = taskService.getAllByAccountId(account.getId());
            account.setTasks(accountTasks);
        }
        return accountMapper.toListResponses(accounts);
    }

    @CacheEvict(value = "accounts", key = "#accountId")
    @Override
    public void update(UUID accountId, AccountRequest accountRequest) {
        Account updatableAccount = accountMapper.toModel(accountRequest);
        accountRepository.update(accountId, updatableAccount);
    }

    @Transactional
    @CacheEvict(value = "accounts", key = "#accountId")
    @Override
    public void delete(UUID accountId) {
        accountRepository.delete(accountId);
        taskService.deleteAllByAccountId(accountId);
    }

    @Override
    public TaskResponse createTask(UUID accountId, TaskRequest taskRequest) {
        return taskService.create(accountId, taskRequest);
    }

    @Cacheable(value = "tasks", key = "#taskId")
    @Override
    public TaskResponse getTask(UUID accountId, UUID taskId) {
        return taskService.get(taskId);
    }

    @Override
    public List<TaskResponse> getAllTasks(UUID accountId, int page, int size) {
        return taskService.getAllPaged(accountId, page, size);
    }

    @CacheEvict(value = "tasks", key = "#taskId")
    @Override
    public void updateTask(UUID accountId, UUID taskId, UpdateTaskRequest taskRequest) {
        taskService.update(taskId, taskRequest);
    }

    @CacheEvict(value = "tasks", key = "#taskId")
    @Override
    public void deleteTask(UUID accountId, UUID taskId) {
        taskService.delete(taskId);
    }
}
