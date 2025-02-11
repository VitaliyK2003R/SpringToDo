package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.Task;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface PagingAndSortingTaskRepository extends PagingAndSortingRepository<Task, UUID> {
}
