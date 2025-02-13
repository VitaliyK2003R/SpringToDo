package com.emobile.springtodo.util;

import com.emobile.springtodo.dto.request.TaskRequest;
import com.emobile.springtodo.dto.request.UpdateTaskRequest;
import com.emobile.springtodo.dto.response.AccountTaskResponse;
import com.emobile.springtodo.dto.response.TaskResponse;
import com.emobile.springtodo.model.Account;
import com.emobile.springtodo.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "start", target = "start")
    @Mapping(source = "finish", target = "finish")
    @Mapping(target = "account", ignore = true)
    Task toModel(TaskRequest taskRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "start", target = "start")
    @Mapping(source = "finish", target = "finish")
    @Mapping(source = "accountId", target = "account", qualifiedByName = "mapToAccount")
    Task toModel(UpdateTaskRequest taskRequest);

    @Named("mapToAccount")
    default Account mapAccountId(UUID accountId) {
        return Account.builder().id(accountId).build();
    }

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "start", target = "start")
    @Mapping(source = "finish", target = "finish")
    @Mapping(source = "account.id", target = "accountId")
    TaskResponse toResponse(Task task);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "start", target = "start")
    @Mapping(source = "finish", target = "finish")
    AccountTaskResponse toAccountResponse(Task task);
    List<AccountTaskResponse> toListAccountResponses(List<Task> tasks);

    default Page<TaskResponse> toPagedResponses(Page<Task> pagedTasks) {
        List<TaskResponse> taskResponses = pagedTasks.get().map(this::toResponse).toList();
        return new PageImpl<>(taskResponses, pagedTasks.getPageable(), pagedTasks.getTotalElements());
    }
}
