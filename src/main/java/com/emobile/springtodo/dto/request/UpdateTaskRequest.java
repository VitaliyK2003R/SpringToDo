package com.emobile.springtodo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NotNull
@AllArgsConstructor
@Schema(description = "DTO задачи для обновления")
public class UpdateTaskRequest {
    @NotBlank
    @Schema(description = "название задачи")
    private String name;

    @Schema(description = "описание задачи")
    private String title;

    @NotNull
    @Schema(description = "время начала задачи задачи")
    private LocalDateTime start;

    @NotNull
    @Schema(description = "время окончания задачи задачи")
    private LocalDateTime finish;

    @NotNull
    @Schema(description = "идентификатор аккаунта, которому принадлежит задача")
    private UUID accountId;
}
