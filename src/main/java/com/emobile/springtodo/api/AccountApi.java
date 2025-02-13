package com.emobile.springtodo.api;

import com.emobile.springtodo.dto.request.AccountRequest;
import com.emobile.springtodo.dto.request.TaskRequest;
import com.emobile.springtodo.dto.request.UpdateTaskRequest;
import com.emobile.springtodo.dto.response.AccountResponse;
import com.emobile.springtodo.dto.response.TaskResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@OpenAPIDefinition(
        info = @Info(
                title = "Account api",
                description = "API аккаунтков и их задач",
                version = "1.0.0",
                contact = @Contact(
                        name = "Komissarov Vitaliy",
                        email = "vk_goodwork@mail.ru"
                )
        )
)
@RequestMapping("/api/v1/accounts")
public interface AccountApi {
    @PostMapping
    @Operation(summary = "создание аккаунта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "аккаунт создан"),
            @ApiResponse(responseCode = "201", description = "аккаунт создан"),
            @ApiResponse(responseCode = "400", description = "ошибка валидации")
    })
    ResponseEntity<AccountResponse> create(
            @Parameter(description = "Входные данные при создании аккаунта")
            @RequestBody @Valid AccountRequest accountRequest);

    @GetMapping("/{accountId}")
    @Operation(summary = "Получение данных аккаунта с его задачами")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "информация об аккаунте получена"),
            @ApiResponse(responseCode = "201", description = "информация об аккаунте получена"),
            @ApiResponse(responseCode = "400", description = "ошибка валидации")
    })
    ResponseEntity<AccountResponse> get(@Parameter(description ="Идентификатор аккаунта") @PathVariable(name = "accountId") UUID accountId);

    @GetMapping
    @Operation(summary = "Получение всех данных о всех аккаунтах")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "информация об аккаунтах получена"),
            @ApiResponse(responseCode = "201", description = "информация об аккаунтах получена"),
    })
    ResponseEntity<List<AccountResponse>> getAll();

    @PutMapping("/{accountId}")
    @Operation(summary = "Обновление данных аккаунта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "аккаунт обновлен"),
            @ApiResponse(responseCode = "201", description = "аккаунт обновлен"),
            @ApiResponse(responseCode = "400", description = "ошибка валидации")
    })
    void update(
            @Parameter(description = "Идентификатор аккаунта") @PathVariable(name = "accountId") UUID accountId,
            @Parameter(description =    "Входные данные при обновлении аккаунта")
            @RequestBody @Valid AccountRequest accountRequest);

    @DeleteMapping("/{accountId}")
    @Operation(summary = "Удаление аккаунта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "аккаунт удален"),
            @ApiResponse(responseCode = "201", description = "аккаунт удален"),
            @ApiResponse(responseCode = "400", description = "ошибка валидации")
    })
    void delete(@Parameter(description = "Идентификатор аккаунта") @PathVariable(name = "accountId") UUID accountId);

    @PostMapping("/{accountId}/tasks")
    @Operation(summary = "Создание задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "задача создан"),
            @ApiResponse(responseCode = "201", description = "задача создан"),
            @ApiResponse(responseCode = "400", description = "ошибка валидации")
    })
    ResponseEntity<TaskResponse> createTask(
            @Parameter(description = "Идентификатор аккаунта")
            @PathVariable(name = "accountId") UUID accountId, @RequestBody @Valid TaskRequest taskRequest);

    @GetMapping("/{accountId}/tasks/{taskId}")
    @Operation(summary = "Получение задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "информация о задаче получена"),
            @ApiResponse(responseCode = "201", description = "информация о задаче получена"),
            @ApiResponse(responseCode = "400", description = "ошибка валидации")
    })
    ResponseEntity<TaskResponse> getTask(
            @Parameter(description = "Идентификатор аккаунта")
            @PathVariable(name = "accountId") UUID accountId,
            @Parameter(description = "Идентификатор задачи")
            @PathVariable UUID taskId);

    @GetMapping("/{accountId}/tasks")
    @Operation(summary = "Получение всех задач аккаунта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "информация о задачах получена"),
            @ApiResponse(responseCode = "201", description = "информация о задачах получена"),
    })
    ResponseEntity<Page<TaskResponse>> getAllTasks(
            @Parameter(description = "Идентификатор аккаунта") @PathVariable(name = "accountId") UUID accountId,
            @Parameter(description = "Номер страницы, начиная с 0")
            @RequestParam(required = false, defaultValue = "0") @Min(0) @Max(50) int page,
            @Parameter(description = "Количество записей на одной странице")
            @RequestParam(required = false, defaultValue = "5") @Min(1) @Max(20) int size);

    @PutMapping("/{accountId}/tasks/{taskId}")
    @Operation(summary = "Обновление задачи аккаунта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "задача обновлена"),
            @ApiResponse(responseCode = "201", description = "задача обновлена"),
            @ApiResponse(responseCode = "400", description = "ошибка валидации")
    })
    void updateTask(
            @Parameter(description = "Идентификатор аккаунта") @PathVariable(name = "accountId") UUID accountId,
            @Parameter(description = "Идентификатор задачи") @PathVariable(name = "taskId") UUID taskId,
            @Parameter(description = "Входные данные при обновлении задачи")
            @RequestBody @Valid UpdateTaskRequest taskRequest);

    @DeleteMapping("/{accountId}/tasks/{taskId}")
    @Operation(summary = "Удаление задачи аккаунта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "задача удалена"),
            @ApiResponse(responseCode = "201", description = "задача удалена"),
            @ApiResponse(responseCode = "400", description = "ошибка валидации")
    })
    void deleteTask(
            @Parameter(description = "Идентификатор аккаунта") @PathVariable(name = "accountId") UUID accountId,
            @Parameter(description = "Идентификатор задачи") @PathVariable(name = "taskId") UUID taskId);
}
