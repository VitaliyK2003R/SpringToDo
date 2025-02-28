package com.emobile.springtodo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO задачи для запроса")
public class TaskRequest {
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
}
