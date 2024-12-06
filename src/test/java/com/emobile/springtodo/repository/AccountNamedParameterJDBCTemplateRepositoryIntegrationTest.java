package com.emobile.springtodo.repository;

import com.emobile.springtodo.exception.AccountNotFoundException;
import com.emobile.springtodo.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
public class AccountNamedParameterJDBCTemplateRepositoryIntegrationTest {
    @Autowired
    private AccountNamedParameterJDBCTemplateRepository accountNamedParameterJDBCTemplateRepository;
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:12-alpine");

    @Test
    public void successCreatingAccountTest() {
        Account expectedAccount = buildAccount("account");

        Account actualAccount = accountNamedParameterJDBCTemplateRepository.create(expectedAccount);

        assertNotNull(actualAccount);
        assertNotNull(actualAccount.getId());
        assertEquals("account", actualAccount.getUsername());
        assertNull(actualAccount.getTasks());
    }

    @Test
    public void successGettingAccountTest() {
        Account expectedAccount = buildAccount("account");
        expectedAccount = createAccount(expectedAccount);

        Account actualAccount = accountNamedParameterJDBCTemplateRepository.get(expectedAccount.getId());

        assertNotNull(actualAccount);
        assertNotNull(actualAccount.getId());
        assertEquals("account", actualAccount.getUsername());
        assertNull(actualAccount.getTasks());
    }

    @Test
    public void successGettingAllAccountsTest() {
        Account firstAccount = createAccount(buildAccount("firstAccount"));
        Account secondAccount = createAccount(buildAccount("secondAccount"));

        List<Account> actualAccounts = accountNamedParameterJDBCTemplateRepository.getAll();

        assertNotNull(actualAccounts);
        assertFalse(actualAccounts.isEmpty());
        assertEquals(List.of(firstAccount, secondAccount), actualAccounts);
    }

    @Test
    public void successUpdatingAccountTest() {
        Account expectedAccount = createAccount(buildAccount("account"));

        expectedAccount.setUsername("updatedUsername");
        accountNamedParameterJDBCTemplateRepository.update(expectedAccount.getId(), expectedAccount);

        Account actualAccount = accountNamedParameterJDBCTemplateRepository.get(expectedAccount.getId());
        assertNotNull(actualAccount);
        assertEquals(expectedAccount.getId(), actualAccount.getId());
        assertEquals("updatedUsername", actualAccount.getUsername());
    }

    @Test
    public void successDeletingAccountTest() {
        Account deletableAccount = createAccount(buildAccount("account"));

        accountNamedParameterJDBCTemplateRepository.delete(deletableAccount.getId());

        assertThrows(
                        AccountNotFoundException.class,
                        () -> accountNamedParameterJDBCTemplateRepository.get(deletableAccount.getId())
                    );
    }

    private Account createAccount(Account account) {
        return accountNamedParameterJDBCTemplateRepository.create(account);
    }

    private Account buildAccount(String username) {
        return Account.builder().username(username).build();
    }
}
