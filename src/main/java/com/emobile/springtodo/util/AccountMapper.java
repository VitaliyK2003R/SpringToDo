package com.emobile.springtodo.util;

import com.emobile.springtodo.dto.request.AccountRequest;
import com.emobile.springtodo.dto.response.AccountResponse;
import com.emobile.springtodo.dto.response.AccountTaskResponse;
import com.emobile.springtodo.dto.response.TaskResponse;
import com.emobile.springtodo.model.Account;
import com.emobile.springtodo.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AccountMapper {
    @Autowired
    private TaskMapper taskMapper;

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "username", target = "username")
    @Mapping(target = "tasks", ignore = true)
    public abstract Account toModel(AccountRequest accountRequest);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "tasks", target = "accountTaskResponses", qualifiedByName = "mapTasksToAccountTaskResponses")
    public abstract AccountResponse toResponse(Account account);

    @Named(value = "mapTasksToAccountTaskResponses")
    public List<AccountTaskResponse> mapTasksToAccountTaskResponses(List<Task> tasks) {
        if (tasks == null) {
            return Collections.emptyList();
        }
        return taskMapper.toListAccountResponses(tasks);
    }

    public abstract List<AccountResponse> toListResponses(List<Account> accounts);
}
