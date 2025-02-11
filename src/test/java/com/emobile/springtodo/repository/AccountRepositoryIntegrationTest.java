package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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

        Account actualAccount = accountRepository.save(expectedAccount);

        assertNotNull(actualAccount);
        assertNotNull(actualAccount.getId());
        assertEquals("account", actualAccount.getUsername());
        assertNull(actualAccount.getTasks());
    }

    @Test
    public void successGettingAccountTest() {
        Account expectedAccount = buildAccount("account");
        expectedAccount = createAccount(expectedAccount);

        Account actualAccount = accountRepository.findById(expectedAccount.getId()).orElseThrow();

        assertNotNull(actualAccount);
        assertNotNull(actualAccount.getId());
        assertEquals("account", actualAccount.getUsername());
        assertNull(actualAccount.getTasks());
    }

    @Test
    public void successGettingAllAccountsTest() {
        Account firstAccount = createAccount(buildAccount("firstAccount"));
        Account secondAccount = createAccount(buildAccount("secondAccount"));

        List<Account> actualAccounts = accountRepository.findAll();

        assertNotNull(actualAccounts);
        assertFalse(actualAccounts.isEmpty());
        assertEquals(List.of(firstAccount, secondAccount), actualAccounts);
    }

    @Test
    public void successUpdatingAccountTest() {
        Account expectedAccount = createAccount(buildAccount("account"));

        expectedAccount.setUsername("updatedUsername");
        accountRepository.save(expectedAccount);

        Optional<Account> optionalActualAccount = accountRepository.findById(expectedAccount.getId());
        assertTrue(optionalActualAccount.isPresent());
        Account actualAccount = optionalActualAccount.get();
        assertNotNull(actualAccount);
        assertEquals(expectedAccount.getId(), actualAccount.getId());
        assertEquals("updatedUsername", actualAccount.getUsername());
    }

    @Test
    public void successDeletingAccountTest() {
        Account deletableAccount = createAccount(buildAccount("account"));

        accountRepository.deleteById(deletableAccount.getId());
        UUID deletedAccountId = deletableAccount.getId();

        assertNotNull(deletedAccountId);
        assertFalse(accountRepository.findById(deletedAccountId).isPresent());
    }

    private Account createAccount(Account account) {
        return accountRepository.saveAndFlush(account);
    }

    private Account buildAccount(String username) {
        return Account.builder().username(username).build();
    }
}
