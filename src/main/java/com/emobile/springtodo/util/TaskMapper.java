package com.emobile.springtodo.util;

import com.emobile.springtodo.dto.request.TaskRequest;
import com.emobile.springtodo.dto.request.UpdateTaskRequest;
import com.emobile.springtodo.dto.response.AccountTaskResponse;
import com.emobile.springtodo.dto.response.TaskResponse;
import com.emobile.springtodo.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "start", target = "start")
    @Mapping(source = "finish", target = "finish")
    Task toModel(TaskRequest taskRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "start", target = "start")
    @Mapping(source = "finish", target = "finish")
    @Mapping(source = "accountId", target = "accountId")
    Task toModel(UpdateTaskRequest taskRequest);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "start", target = "start")
    @Mapping(source = "finish", target = "finish")
    @Mapping(source = "accountId", target = "accountId")
    TaskResponse toResponse(Task task);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "start", target = "start")
    @Mapping(source = "finish", target = "finish")
    AccountTaskResponse toAccountResponse(Task task);

    List<TaskResponse> toListResponses(List<Task> tasks);
    List<AccountTaskResponse> toListAccountResponses(List<Task> tasks);
}
