package com.emobile.springtodo.repository;

import com.emobile.springtodo.exception.AccountNotFoundException;
import com.emobile.springtodo.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class AccountRepositoryIntegrationTest {
    @Autowired
    private AccountRepository accountRepository;
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:12-alpine");

    @Test
    public void successCreatingAccountTest() {
        Account expectedAccount = buildAccount("account");

        Account actualAccount = accountRepository.create(expectedAccount);

        assertNotNull(actualAccount);
        assertNotNull(actualAccount.getId());
        assertEquals("account", actualAccount.getUsername());
        assertNull(actualAccount.getTasks());

        deleteAccount(actualAccount.getId());
    }

    @Test
    public void successGettingAccountTest() {
        Account expectedAccount = buildAccount("account");
        expectedAccount = createAccount(expectedAccount);

        Account actualAccount = accountRepository.get(expectedAccount.getId());

        assertNotNull(actualAccount);
        assertNotNull(actualAccount.getId());
        assertEquals("account", actualAccount.getUsername());
        assertNull(actualAccount.getTasks());

        deleteAccount(actualAccount.getId());
    }

    @Test
    public void successGettingAllAccountsTest() {
        Account firstAccount = createAccount(buildAccount("firstAccount"));
        Account secondAccount = createAccount(buildAccount("secondAccount"));

        List<Account> actualAccounts = accountRepository.getAll();

        assertNotNull(actualAccounts);
        assertFalse(actualAccounts.isEmpty());
        assertEquals(List.of(firstAccount, secondAccount), actualAccounts);

        deleteAccounts(List.of(firstAccount.getId(), secondAccount.getId()));
    }

    @Test
    public void successUpdatingAccountTest() {
        Account expectedAccount = createAccount(buildAccount("account"));

        expectedAccount.setUsername("updatedUsername");
        accountRepository.update(expectedAccount.getId(), expectedAccount);

        Account actualAccount = accountRepository.get(expectedAccount.getId());
        assertNotNull(actualAccount);
        assertEquals(expectedAccount.getId(), actualAccount.getId());
        assertEquals("updatedUsername", actualAccount.getUsername());

        deleteAccount(actualAccount.getId());
    }

    @Test
    public void successDeletingAccountTest() {
        Account deletableAccount = createAccount(buildAccount("account"));

        accountRepository.delete(deletableAccount.getId());
        UUID deletedAccountId = deletableAccount.getId();

        assertNotNull(deletedAccountId);
        assertThrows(
                AccountNotFoundException.class,
                () -> accountRepository.get(deletedAccountId)
        );
    }

    private Account createAccount(Account account) {
        return accountRepository.create(account);
    }

    private void deleteAccount(UUID accountId) {
        accountRepository.delete(accountId);
    }

    private void deleteAccounts(List<UUID> ids) {
        for (UUID id: ids) {
            deleteAccount(id);
        }
    }

    private Account buildAccount(String username) {
        return Account.builder().username(username).build();
    }
}
