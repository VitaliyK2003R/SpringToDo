package com.emobile.springtodo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AccountTaskResponse(
        @Schema(description = "идентификатор задачи")
        UUID id,
        @Schema(description = "название задачи")
        String name,
        @Schema(description = "описание задачи")
        String title,
        @Schema(description = "время начала задачи задачи")
        LocalDateTime start,
        @Schema(description = "время окончания задачи задачи")
        LocalDateTime finish) {
}
