package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.request.AccountRequest;
import com.emobile.springtodo.dto.request.TaskRequest;
import com.emobile.springtodo.dto.request.UpdateTaskRequest;
import com.emobile.springtodo.dto.response.AccountResponse;
import com.emobile.springtodo.dto.response.TaskResponse;
import com.emobile.springtodo.exception.AccountNotFoundException;
import com.emobile.springtodo.model.Account;
import com.emobile.springtodo.model.Task;
import com.emobile.springtodo.repository.AccountRepository;
import com.emobile.springtodo.util.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
        creatableAccount = accountRepository.save(creatableAccount);
        return accountMapper.toResponse(creatableAccount);
    }

    @Cacheable(value = "accounts", key = "#accountId")
    @Override
    public AccountResponse get(UUID accountId) {
        Account account = getAccount(accountId);
        List<Task> accountTasks = taskService.getAll(accountId);
        account.setTasks(accountTasks);
        return accountMapper.toResponse(account);
    }

    @Override
    public List<AccountResponse> getAll() {
        List<Account> accounts = accountRepository.findAll();
        for (Account account: accounts) {
            List<Task> accountTasks = taskService.getAll(account.getId());
            account.setTasks(accountTasks);
        }
        return accountMapper.toListResponses(accounts);
    }

    @CacheEvict(value = "accounts", key = "#accountId")
    @Override
    public void update(UUID accountId, AccountRequest accountRequest) throws AccountNotFoundException {
        Account updatedAccount = accountMapper.toModel(accountRequest);
        Account updatableAccount = getAccount(accountId);
        updatableAccount.setUsername(updatedAccount.getUsername());
        updatableAccount.setTasks(updatedAccount.getTasks());
        accountRepository.save(updatableAccount);
    }

    @Transactional
    @CacheEvict(value = "accounts", key = "#accountId")
    @Override
    public void delete(UUID accountId) {
        accountRepository.deleteById(accountId);
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
    public Page<TaskResponse> getAllTasks(UUID accountId, int page, int size) {
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

    private Account getAccount(UUID accountId) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException(accountId);
        }
        return optionalAccount.get();
    }
}
