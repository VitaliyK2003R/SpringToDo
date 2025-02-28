package com.emobile.springtodo.dto.response;

import lombok.Builder;

@Builder
public record ExceptionResponse(String message) {
}
