package com.emobile.springtodo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Builder
@Schema(description = "DTO аккаунта для ответа")
public record AccountResponse(
        @Schema(description = "идентификатор аккаунта")
        UUID id,
        @Schema(description = "имя аккаунта")
        String username,
        @Schema(description = "Задачи аккаунта")
        List<AccountTaskResponse> accountTaskResponses) implements Serializable {
}
