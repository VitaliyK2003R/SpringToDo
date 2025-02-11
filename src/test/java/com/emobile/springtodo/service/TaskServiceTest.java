package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.request.TaskRequest;
import com.emobile.springtodo.dto.request.UpdateTaskRequest;
import com.emobile.springtodo.dto.response.TaskResponse;
import com.emobile.springtodo.model.Account;
import com.emobile.springtodo.model.Task;
import com.emobile.springtodo.repository.TaskRepository;
import com.emobile.springtodo.util.TaskMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @InjectMocks
    private TaskServiceImpl taskService;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private TaskRepository taskRepository;

    @Test
    public void successCreatingTask() {
        UUID accountId = UUID.randomUUID();
        TaskRequest taskRequest = TaskRequest.builder().name("name").build();
        Task mappedRequest = Task.builder().name("name").build();
        TaskResponse taskResponse = TaskResponse.builder().accountId(accountId).name("name").build();

        when(taskMapper.toModel(taskRequest)).thenReturn(mappedRequest);
        when(taskRepository.save(mappedRequest)).thenReturn(mappedRequest);
        when(taskMapper.toResponse(mappedRequest)).thenReturn(taskResponse);

        assertDoesNotThrow(() -> taskService.create(accountId, taskRequest));
        assertEquals("name", taskService.create(accountId, taskRequest).name());
        assertEquals(accountId, taskService.create(accountId, taskRequest).accountId());
    }

    @Test
    public void successGettingTask() {
        UUID taskId = UUID.randomUUID();
        Task task = Task.builder().id(taskId).build();
        TaskResponse taskResponse = TaskResponse.builder().id(taskId).build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        assertDoesNotThrow(() -> taskService.get(taskId));
        assertEquals(taskId, taskService.get(taskId).id());
    }

    @Test
    public void successGettingAllAccountTasksTest() {
        UUID accountId = UUID.randomUUID();
        List<Task> tasks = Collections.emptyList();
        List<TaskResponse> taskResponses = Collections.emptyList();
        Pageable pageable = PageRequest.of(0,5);
        Page<Task> pagedTasks = new PageImpl<>(tasks, pageable, 0);
        Page<TaskResponse> pagedTaskResponses = new PageImpl<>(taskResponses, pageable, 0);

        when(taskRepository.findAllByAccount_Id(accountId, pageable)).thenReturn(pagedTasks);
        when(taskMapper.toPagedResponses(pagedTasks)).thenReturn(pagedTaskResponses);

        assertDoesNotThrow(() -> taskService.getAllPaged(accountId, 0, 5));
        assertEquals(0, taskService.getAllPaged(accountId,0,5).getTotalElements());
    }

    @Test
    public void successUpdatingTaskTest() {
        UUID taskId = UUID.randomUUID();
        UpdateTaskRequest updateTaskRequest = UpdateTaskRequest.builder().build();
        Task task = Task.builder().build();

        when(taskMapper.toModel(updateTaskRequest)).thenReturn(task);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        assertDoesNotThrow(() -> taskService.update(taskId, updateTaskRequest));
    }

    @Test
    public void successDeletingTaskTest() {
        UUID taskId = UUID.randomUUID();

        doNothing().when(taskRepository).deleteById(taskId);

        assertDoesNotThrow(() -> taskService.delete(taskId));
    }

    @Test
    public void successGettingAllByAccountIdTest() {
        UUID accountId = UUID.randomUUID();
        Account account = Account.builder().id(accountId).build();
        UUID taskId = UUID.randomUUID();
        List<Task> tasks = List.of(Task.builder().id(taskId).account(account).build());

        when(taskRepository.findAllByAccount_Id(accountId)).thenReturn(tasks);

        assertDoesNotThrow(() -> taskService.getAll(accountId));
        List<Task> responses = taskService.getAll(accountId);
        assertEquals(1, responses.size());
        assertEquals(taskId, responses.get(0).getId());
    }

    @Test
    public void successDeletingAllByAccountIdTest() {
        UUID accountId = UUID.randomUUID();

        doNothing().when(taskRepository).deleteAllByAccountId(accountId);

        assertDoesNotThrow(() -> taskService.deleteAllByAccountId(accountId));
    }
}
