package com.emobile.springtodo.repository;

import com.emobile.springtodo.exception.SQLProcessChangesException;
import com.emobile.springtodo.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TaskNamedParameterJDBCTemplateRepository implements TaskRepository {
    private static final String QUERY_TASK_ID_PARAMETER_NAME = "id";
    private static final String QUERY_EXTENDED_TASK_ID_PARAMETER_NAME = "taskId";
    private static final String QUERY_TASK_TITLE_PARAMETER_NAME = "title";
    private static final String QUERY_TASK_NAME_PARAMETER_NAME = "name";
    private static final String QUERY_TASK_START_PARAMETER_NAME = "start";
    private static final String QUERY_TASK_FINISH_PARAMETER_NAME = "finish";
    private static final String TASK_ID_ATTRIBUTE_NAME = QUERY_TASK_ID_PARAMETER_NAME;
    private static final String TASK_TITLE_ATTRIBUTE_NAME = QUERY_TASK_TITLE_PARAMETER_NAME;
    private static final String TASK_NAME_ATTRIBUTE_NAME = QUERY_TASK_NAME_PARAMETER_NAME;
    private static final String TASK_START_ATTRIBUTE_NAME = QUERY_TASK_START_PARAMETER_NAME;
    private static final String TASK_FINISH_ATTRIBUTE_NAME = QUERY_TASK_FINISH_PARAMETER_NAME;
    private static final String TASK_ACCOUNT_ID_ATTRIBUTE_NAME = "account_id";
    private static final String QUERY_TASK_ACCOUNT_ID_PARAMETER_NAME = "accountId";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final RowMapper<Task> ROW_MAPPER = (resultSet, rowNum) -> Task.builder()
            .id((UUID) resultSet.getObject(TASK_ID_ATTRIBUTE_NAME))
            .title(resultSet.getString(TASK_TITLE_ATTRIBUTE_NAME))
            .name(resultSet.getString(TASK_NAME_ATTRIBUTE_NAME))
            .start(resultSet.getTimestamp(TASK_START_ATTRIBUTE_NAME).toLocalDateTime())
            .finish(resultSet.getTimestamp(TASK_FINISH_ATTRIBUTE_NAME).toLocalDateTime())
            .accountId((UUID) resultSet.getObject(TASK_ACCOUNT_ID_ATTRIBUTE_NAME))
            .build();

    @Override
    public Task create(Task task) {
        Map<String, Object> taskParameters = Map.of(QUERY_TASK_NAME_PARAMETER_NAME, task.getName(),
                QUERY_TASK_TITLE_PARAMETER_NAME, task.getTitle(),
                QUERY_TASK_START_PARAMETER_NAME, task.getStart(),
                QUERY_TASK_FINISH_PARAMETER_NAME, task.getFinish(),
                QUERY_TASK_ACCOUNT_ID_PARAMETER_NAME, task.getAccountId());
        SqlParameterSource parameterSource = new MapSqlParameterSource(taskParameters);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int countChanges = namedParameterJdbcTemplate.update(
                """
                        insert into task (name, title, start, finish, account_id)
                        values (:name, :title, :start, :finish, :accountId)
                        returning task.id
                        """,
                parameterSource,
                keyHolder);
        if (countChanges != 1) {
            throw new SQLProcessChangesException("Invalid result count changes in data base");
        }
        UUID generatedUUID = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get(QUERY_TASK_ID_PARAMETER_NAME);
        task.setId(generatedUUID);
        return task;
    }

    @Override
    public Task get(UUID taskId) {
        return namedParameterJdbcTemplate.queryForObject(
                "select * from task t where t.id = :taskId",
                Map.of(QUERY_EXTENDED_TASK_ID_PARAMETER_NAME, taskId),
                ROW_MAPPER
        );
    }

    @Override
    public void update(UUID taskId, Task task) {
        int countChanges = namedParameterJdbcTemplate.update(
                """
                            update task
                            set name = :name, title = :title, start = :start, finish = :finish, account_id = :accountId
                            where task.id = :taskId
                        """,
                Map.of(QUERY_TASK_NAME_PARAMETER_NAME, task.getName(),
                        QUERY_TASK_TITLE_PARAMETER_NAME, task.getTitle(),
                        QUERY_TASK_START_PARAMETER_NAME, task.getStart(),
                        QUERY_TASK_FINISH_PARAMETER_NAME, task.getFinish(),
                        QUERY_EXTENDED_TASK_ID_PARAMETER_NAME, taskId,
                        QUERY_TASK_ACCOUNT_ID_PARAMETER_NAME, task.getAccountId()));
        if (countChanges != 1) {
            throw new SQLProcessChangesException("Invalid result count changes in data base");
        }
    }

    @Override
    public void delete(UUID taskId) {
        namedParameterJdbcTemplate.update(
                "delete from task where task.id = :taskId", Map.of(QUERY_EXTENDED_TASK_ID_PARAMETER_NAME, taskId));
    }

    @Override
    public List<Task> getAllPagedByAccountId(UUID accountId, int page, int size) {
        return namedParameterJdbcTemplate.query(
                "select * from task where task.account_id = :accountId limit :size offset :page",
                Map.of(QUERY_TASK_ACCOUNT_ID_PARAMETER_NAME, accountId, "size", size, "page", page * size),
                ROW_MAPPER);
    }

    @Override
    public void deleteAllByAccountId(UUID accountId) {
        namedParameterJdbcTemplate.update(
                "delete from task where task.account_id = :accountId",
                Map.of(QUERY_TASK_ACCOUNT_ID_PARAMETER_NAME, accountId));
    }
}
