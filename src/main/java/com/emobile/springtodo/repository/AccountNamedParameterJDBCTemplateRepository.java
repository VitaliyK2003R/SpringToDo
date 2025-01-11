package com.emobile.springtodo.repository;

import com.emobile.springtodo.exception.AccountAlreadyExistsException;
import com.emobile.springtodo.exception.AccountNotFoundException;
import com.emobile.springtodo.exception.SQLProcessChangesException;
import com.emobile.springtodo.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class AccountNamedParameterJDBCTemplateRepository implements AccountRepository {
    private static final String QUERY_ACCOUNT_NAME_PARAMETER_NAME = "username";
    private static final String QUERY_ACCOUNT_ID_PARAMETER_NAME = "accountId";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final RowMapper<Account> ROW_MAPPER = (resultSet, rowNum) -> Account.builder()
            .id((UUID) resultSet.getObject("id"))
            .username(resultSet.getString(QUERY_ACCOUNT_NAME_PARAMETER_NAME))
            .build();

    @Override
    public Account create(Account account) {
        checkAccountExists(account.getUsername());
        Map<String, Object> accountParameters = Map.of(QUERY_ACCOUNT_NAME_PARAMETER_NAME, account.getUsername());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource parameterSource = new MapSqlParameterSource(accountParameters);
        int countChanges = namedParameterJdbcTemplate.update(
                                                            "insert into account (username) values (:username)",
                                                                parameterSource,
                                                                keyHolder
                                                            );
        if (countChanges != 1) {
            throw new SQLProcessChangesException("Invalid result count changes in data base");
        }
        UUID generatedUUID = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        account.setId(generatedUUID);
        return account;
    }

    private void checkAccountExists(String username) {
        if (existsByUsername(username)) {
            throw new AccountAlreadyExistsException(username);
        }
    }

    private boolean existsByUsername(String username) {
        try {
            namedParameterJdbcTemplate.queryForObject(
                    "select * from account where account.username = :username",
                    Map.of(QUERY_ACCOUNT_NAME_PARAMETER_NAME, username),
                    ROW_MAPPER
            );
            return true;
        } catch (EmptyResultDataAccessException ex) {
            return false;
        }
    }

    @Override
    public Account get(UUID accountId) {
        checkAccountExists(accountId);
        return namedParameterJdbcTemplate.queryForObject(
                "select * from account where account.id = :accountId",
                Map.of(QUERY_ACCOUNT_ID_PARAMETER_NAME, accountId),
                ROW_MAPPER
        );
    }

    @Override
    public List<Account> getAll() {
        return namedParameterJdbcTemplate.query("select * from account", ROW_MAPPER);
    }

    @Override
    public void update(UUID accountId, Account accountTask) {
        checkAccountExists(accountId);
        int countChanges = namedParameterJdbcTemplate.update(
                "update account set username = :username where account.id = :accountId",
                Map.of(QUERY_ACCOUNT_NAME_PARAMETER_NAME, accountTask.getUsername(),
                        QUERY_ACCOUNT_ID_PARAMETER_NAME, accountId)
        );
        if (countChanges != 1) {
            throw new SQLProcessChangesException("Invalid result count changes in data base");
        }
    }

    private void checkAccountExists(UUID accountId) {
        try {
            namedParameterJdbcTemplate.queryForObject(
                    "select * from account where account.id = :accountId",
                    Map.of(QUERY_ACCOUNT_ID_PARAMETER_NAME, accountId),
                    ROW_MAPPER);
        } catch (EmptyResultDataAccessException ex) {
            throw new AccountNotFoundException(accountId);
        }
    }

    @Override
    public void delete(UUID accountId) {
        checkAccountExists(accountId);
        namedParameterJdbcTemplate.update(
                "delete from account where account.id = :accountId",
                Map.of(QUERY_ACCOUNT_ID_PARAMETER_NAME, accountId)
        );
    }
}
