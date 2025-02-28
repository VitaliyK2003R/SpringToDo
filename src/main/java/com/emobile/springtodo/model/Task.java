package com.emobile.springtodo.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private UUID id;
    private String name;
    private String title;
    private LocalDateTime start;
    private LocalDateTime finish;
    private UUID accountId;
}
