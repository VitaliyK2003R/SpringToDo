package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.request.TaskRequest;
import com.emobile.springtodo.dto.request.UpdateTaskRequest;
import com.emobile.springtodo.dto.response.TaskResponse;
import com.emobile.springtodo.model.Task;
import com.emobile.springtodo.repository.TaskRepository;
import com.emobile.springtodo.util.TaskMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
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
        when(taskRepository.create(mappedRequest)).thenReturn(mappedRequest);
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

        when(taskRepository.get(taskId)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        assertDoesNotThrow(() -> taskService.get(taskId));
        assertEquals(taskId, taskService.get(taskId).id());
    }

    @Test
    public void successGettingAllAccountTasksTest() {
        UUID accountId = UUID.randomUUID();
        List<Task> tasks = Collections.emptyList();
        List<TaskResponse> taskResponses = Collections.emptyList();

        when(taskService.getAllByAccountId(accountId)).thenReturn(tasks);
        when(taskMapper.toListResponses(tasks)).thenReturn(taskResponses);

        assertDoesNotThrow(() -> taskService.getAllPaged(accountId, 0, 5));
        assertEquals(0, taskService.getAllPaged(accountId, 0, 5).size());
    }

    @Test
    public void successUpdatingTaskTest() {
        UUID taskId = UUID.randomUUID();
        UpdateTaskRequest updateTaskRequest = UpdateTaskRequest.builder().build();
        Task task = Task.builder().build();

        when(taskMapper.toModel(updateTaskRequest)).thenReturn(task);
        doNothing().when(taskRepository).update(taskId, task);

        assertDoesNotThrow(() -> taskService.update(taskId, updateTaskRequest));
    }

    @Test
    public void successDeletingTaskTest() {
        UUID taskId = UUID.randomUUID();

        doNothing().when(taskRepository).delete(taskId);

        assertDoesNotThrow(() -> taskService.delete(taskId));
    }

    @Test
    public void successGettingAllByAccountIdTest() {
        UUID accountId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        List<Task> tasks = List.of(Task.builder().id(taskId).accountId(accountId).build());

        when(taskRepository.getAllPagedByAccountId(accountId, 0, 5)).thenReturn(tasks);

        assertDoesNotThrow(() -> taskService.getAllByAccountId(accountId));
        List<Task> responses = taskService.getAllByAccountId(accountId);
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
