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
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final RowMapper<Task> ROW_MAPPER = (resultSet, rowNum) -> Task.builder()
                                                        .id((UUID) resultSet.getObject("id"))
                                                        .title(resultSet.getString("title"))
                                                        .name(resultSet.getString("name"))
                                                        .start(resultSet.getTimestamp("start").toLocalDateTime())
                                                        .finish(resultSet.getTimestamp("finish").toLocalDateTime())
                                                        .accountId((UUID) resultSet.getObject("account_id"))
                                                        .build();

    @Override
    public Task create(Task task) {
        Map<String, Object> taskParameters = Map.of("name", task.getName(),
                                                    "title", task.getTitle(),
                                                    "start", task.getStart(),
                                                    "finish", task.getFinish(),
                                                    "accountId", task.getAccountId());
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
        UUID generatedUUID = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        task.setId(generatedUUID);
        return task;
    }

    @Override
    public Task get(UUID taskId) {
        return namedParameterJdbcTemplate.queryForObject(
                                                        "select * from task t where t.id = :taskId",
                                                            Map.of("taskId", taskId),
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
            Map.of( "name", task.getName(), "title", task.getTitle(),
                    "start", task.getStart(), "finish", task.getFinish(),
                    "taskId", taskId, "accountId", task.getAccountId()));
        if (countChanges != 1) {
            throw new SQLProcessChangesException("Invalid result count changes in data base");
        }
    }

    @Override
    public void delete(UUID taskId) {
        namedParameterJdbcTemplate.update(
                "delete from task where task.id = :taskId", Map.of("taskId", taskId));
    }

    @Override
    public List<Task> getAllPagedByAccountId(UUID accountId, int page, int size) {
        return namedParameterJdbcTemplate.query(
                "select * from task where task.account_id = :accountId limit :size offset :page",
                Map.of("accountId", accountId, "size", size, "page", page * size),
                ROW_MAPPER);
    }

    @Override
    public void deleteAllByAccountId(UUID accountId) {
        namedParameterJdbcTemplate.update(
                "delete from task where task.account_id = :accountId", Map.of("accountId", accountId));
    }
}
