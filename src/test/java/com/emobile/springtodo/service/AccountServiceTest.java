package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.request.AccountRequest;
import com.emobile.springtodo.dto.request.TaskRequest;
import com.emobile.springtodo.dto.request.UpdateTaskRequest;
import com.emobile.springtodo.dto.response.AccountResponse;
import com.emobile.springtodo.dto.response.AccountTaskResponse;
import com.emobile.springtodo.dto.response.TaskResponse;
import com.emobile.springtodo.exception.AccountAlreadyExistsException;
import com.emobile.springtodo.exception.AccountNotFoundException;
import com.emobile.springtodo.model.Account;
import com.emobile.springtodo.model.Task;
import com.emobile.springtodo.repository.AccountRepository;
import com.emobile.springtodo.util.AccountMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private TaskService taskService;
    @Mock
    private AccountRepository accountRepository;

    @Test
    public void successCreatingAccountTest() {
        UUID generatedAccountId = UUID.randomUUID();
        AccountRequest creatableRequest = new AccountRequest("username");
        Account creatableAccount = Account.builder().id(generatedAccountId).username("username").build();
        AccountResponse createdResponse = AccountResponse.builder()
                                                            .id(generatedAccountId)
                                                            .username("username")
                                                            .accountTaskResponses(Collections.emptyList())
                                                            .build();

        when(accountMapper.toModel(creatableRequest)).thenReturn(creatableAccount);
        when(accountRepository.create(creatableAccount)).thenReturn(creatableAccount);
        when(accountMapper.toResponse(creatableAccount)).thenReturn(createdResponse);

        assertDoesNotThrow(() -> accountService.create(creatableRequest));
        assertEquals(creatableRequest.getUsername(), createdResponse.username());
    }

    @Test
    public void failCreatingAccountTest() {
        AccountRequest creatableRequest = new AccountRequest("username");
        Account creatableAccount = Account.builder().username("username").build();

        when(accountMapper.toModel(creatableRequest)).thenReturn(creatableAccount);
        when(accountRepository.create(creatableAccount)).thenThrow(AccountAlreadyExistsException.class);

        assertThrows(AccountAlreadyExistsException.class, () -> accountService.create(creatableRequest));
    }

    @Test
    public void successGettingAccountTest() {
        UUID accountId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        Account account = Account.builder().id(accountId).build();
        List<Task> tasks = List.of(Task.builder().id(taskId).build());
        AccountResponse expectedResponse = AccountResponse.builder()
                .id(accountId)
                .accountTaskResponses(List.of(AccountTaskResponse.builder().id(taskId).build()))
                .build();

        when(accountRepository.get(accountId)).thenReturn(account);
        when(taskService.getAllByAccountId(accountId)).thenReturn(tasks);
        when(accountMapper.toResponse(account)).thenReturn(expectedResponse);

        assertDoesNotThrow(() -> accountService.get(accountId));
        AccountResponse response = accountService.get(accountId);
        assertEquals(expectedResponse.id(), response.id());
        assertEquals(expectedResponse.accountTaskResponses().size(), response.accountTaskResponses().size());
    }

    @Test
    public void failGettingAccountTest() {
        UUID accountId = UUID.randomUUID();

        when(accountRepository.get(accountId)).thenThrow(AccountNotFoundException.class);

        assertThrows(AccountNotFoundException.class, () -> accountRepository.get(accountId));
    }

    @Test
    public void getAllTest() {
        UUID firstAccountId = UUID.randomUUID();
        UUID secondAccountId = UUID.randomUUID();
        List<Account> accounts = List.of(   Account.builder().id(firstAccountId).build(),
                                            Account.builder().id(secondAccountId).build());
        List<Task> firstAccountTasks = Collections.emptyList();
        List<Task> secondAccountTasks = List.of(Task.builder().build());
        List<AccountResponse> expectedResponses = List.of(
                                            AccountResponse.builder()
                                                .id(firstAccountId)
                                                .accountTaskResponses(Collections.emptyList())
                                                .build(),
                                            AccountResponse.builder()
                                                .id(secondAccountId)
                                                .accountTaskResponses(List.of(AccountTaskResponse.builder().build()))
                                                .build()
                                                        );

        when(accountRepository.getAll()).thenReturn(accounts);
        when(taskService.getAllByAccountId(firstAccountId)).thenReturn(firstAccountTasks);
        when(taskService.getAllByAccountId(secondAccountId)).thenReturn(secondAccountTasks);
        when(accountMapper.toListResponses(accounts)).thenReturn(expectedResponses);

        assertDoesNotThrow(() -> accountService.getAll());
        List<AccountResponse> responses = accountService.getAll();
        assertEquals(responses.get(0).id(), firstAccountId);
        assertEquals(responses.get(1).id(), secondAccountId);
        assertEquals(responses.get(0).accountTaskResponses().size(), 0);
        assertEquals(responses.get(1).accountTaskResponses().size(), 1);
    }

    @Test
    public void successUpdatingTest() {
        UUID accountId = UUID.randomUUID();
        AccountRequest updateRequest = AccountRequest.builder().username("user").build();
        Account mappedUpdateRequest = Account.builder().username("user").build();

        when(accountMapper.toModel(updateRequest)).thenReturn(mappedUpdateRequest);
        doNothing().when(accountRepository).update(accountId, mappedUpdateRequest);

        assertDoesNotThrow(() -> accountService.update(accountId, updateRequest));
    }

    @Test
    public void successDeletingTest() {
        UUID accountId = UUID.randomUUID();

        doNothing().when(accountRepository).delete(accountId);
        doNothing().when(taskService).deleteAllByAccountId(accountId);

        assertDoesNotThrow(() -> accountService.delete(accountId));
    }

    @Test
    public void successCreatingTask() {
        UUID accountId = UUID.randomUUID();
        TaskRequest taskRequest = TaskRequest.builder().build();

        doNothing().when(taskService.create(accountId, taskRequest));

        assertDoesNotThrow(() -> accountService.createTask(accountId, taskRequest));
    }

    @Test
    public void successGettingTask() {
        UUID accountId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        TaskResponse expectedTaskResponse = TaskResponse.builder().id(taskId).build();

        when(taskService.get(taskId)).thenReturn(expectedTaskResponse);

        assertDoesNotThrow(() -> accountService.getTask(accountId, taskId));
        assertEquals(expectedTaskResponse.id(), accountService.getTask(accountId, taskId).id());
    }

    @Test
    public void successGettingAccountTasks() {
        UUID accountId = UUID.randomUUID();
        UUID firstTaskId = UUID.randomUUID();
        UUID secondTaskId = UUID.randomUUID();
        TaskResponse firstTaskResponse = TaskResponse.builder().id(firstTaskId).build();
        TaskResponse secondTaskResponse = TaskResponse.builder().id(secondTaskId).build();
        List<TaskResponse> expectedTaskResponses = List.of(firstTaskResponse, secondTaskResponse);

        when(taskService.getAllPaged(accountId,0,5)).thenReturn(expectedTaskResponses);

        assertDoesNotThrow(() -> accountService.getAllTasks(accountId,0,5));
        List<TaskResponse> responses = accountService.getAllTasks(accountId,0,5);
        assertEquals(expectedTaskResponses.size(), responses.size());
        assertEquals(expectedTaskResponses.get(0).id(), responses.get(0).id());
        assertEquals(expectedTaskResponses.get(1).id(), responses.get(1).id());
    }

    @Test
    public void successUpdatingTaskTest() {
        UUID taskId = UUID.randomUUID();
        UpdateTaskRequest taskRequest = UpdateTaskRequest.builder().build();

        doNothing().when(taskService).update(taskId, taskRequest);

        assertDoesNotThrow(() -> accountService.updateTask(UUID.randomUUID(), taskId, taskRequest));
    }

    @Test
    public void successDeletingTaskTest() {
        UUID taskId = UUID.randomUUID();

        doNothing().when(taskService).delete(taskId);

        assertDoesNotThrow(() -> accountService.deleteTask(UUID.randomUUID(), taskId));
    }
}
