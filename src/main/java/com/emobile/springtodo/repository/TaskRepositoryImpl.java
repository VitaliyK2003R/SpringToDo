package com.emobile.springtodo.repository;

import com.emobile.springtodo.exception.TaskNotFoundException;
import com.emobile.springtodo.model.Task;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {
    private final EntityManager entityManager;

    @Override
    public Task create(Task task) {
        entityManager.getTransaction().begin();
        entityManager.persist(task);
        entityManager.getTransaction().commit();
        return task;
    }

    @Override
    public Task get(UUID taskId) {
        return getTaskWithEntityManager(taskId);
    }

    private Task getTaskWithEntityManager(UUID taskId) throws TaskNotFoundException {
        Task task = entityManager.find(Task.class, taskId);
        if (task == null) {
            throw new TaskNotFoundException();
        }
        return task;
    }

    @Override
    public void update(UUID taskId, Task task) {
        try {
            entityManager.getTransaction().begin();
            updateTask(taskId, task);
            entityManager.getTransaction().commit();
        } catch (TaskNotFoundException ex) {
            entityManager.getTransaction().rollback();
            throw ex;
        }
    }

    private void updateTask(UUID taskId, Task updatedTask) {
        Task updatableTask = getTaskWithEntityManager(taskId);
        updatableTask.setName(updatedTask.getName());
        updatableTask.setTitle(updatedTask.getTitle());
        updatableTask.setStart(updatedTask.getStart());
        updatableTask.setFinish(updatedTask.getFinish());
        updatableTask.setAccount(updatedTask.getAccount());
        entityManager.persist(updatableTask);
    }

    @Override
    public void delete(UUID taskId) {
        Task deletableTask = getTaskWithEntityManager(taskId);
        entityManager.remove(deletableTask);
    }

    @Override
    public Page<Task> getAllPagedByAccountId(UUID accountId, int page, int size) {
        int start, end;
        if (page == 0) {
            start = 0;
            end = size - 1;
        } else {
            start = size + page;
            end = start + size - 1;
        }
        Pageable pageable = PageRequest.of(page, size);
        List<?> taskList = entityManager.createQuery("select task from Task task").getResultList();
        if (taskList.size() < size) {
            return new PageImpl<>((List<Task>) taskList, pageable, taskList.size());
        }
        return new PageImpl<>((List<Task>) taskList.subList(start, end), pageable, taskList.size());
    }

    @Override
    public void deleteAllByAccountId(UUID accountId) {
        entityManager.getTransaction().begin();
        entityManager.createQuery("delete from Task task where task.account.id = ?1")
                                            .setParameter(1, accountId)
                                            .executeUpdate();
    }
}
