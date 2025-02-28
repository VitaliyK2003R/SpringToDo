package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.request.AccountRequest;
import com.emobile.springtodo.dto.response.AccountResponse;
import com.emobile.springtodo.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Transactional
@AutoConfigureCache
@ActiveProfiles("test")
public class AccountControllerIntegrationTest {
    @Autowired
    private AccountService accountService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:12-alpine");
    @Container
    @ServiceConnection(name = "redis")
    static GenericContainer<?> redisContainer
            = new GenericContainer<>(DockerImageName.parse("redis:6.2-alpine")).withExposedPorts(6379);

    @Test
    public void successCreatingAccountTest() throws Exception {
        AccountRequest actualAccountRequest = AccountRequest.builder().username("account").build();

        mockMvc.perform(post("/api/v1/accounts")
                        .content(objectMapper.writeValueAsString(actualAccountRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("account"));
    }

    @Test
    public void successGettingAccountTest() throws Exception {
        AccountResponse expectedAccountResponse = createAccount("account");
        UUID accountId = expectedAccountResponse.id();

        mockMvc.perform(get("/api/v1/accounts/{accountId}", accountId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(accountId.toString()))
                .andExpect(jsonPath("$.username").value("account"));
    }

    @Test
    public void successGettingAllTest() throws Exception {
        List<AccountResponse> accountResponses = createAccounts("account1", "account2");

        mockMvc.perform(get("/api/v1/accounts"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(accountResponses)));
    }

    @Test
    public void successUpdatingTest() throws Exception {
        AccountResponse expectedAccountResponse = createAccount("account");
        UUID accountId = expectedAccountResponse.id();
        AccountRequest accountRequest = AccountRequest.builder().username("updatedAccount").build();

        mockMvc.perform(put("/api/v1/accounts/{accountId}", accountId)
                        .content(objectMapper.writeValueAsString(accountRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/api/v1/accounts/{accountId}", accountId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(expectedAccountResponse.id().toString()))
                .andExpect(jsonPath("$.username").value("updatedAccount"));
    }

    @Test
    public void successDeletingTest() throws Exception {
        AccountResponse accountResponse = createAccount("account");
        UUID accountId = accountResponse.id();

        mockMvc.perform(delete("/api/v1/accounts/{accountId}", accountId))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/api/v1/accounts/{accountId}", accountId))
                .andExpect(status().isBadRequest());
    }

    private List<AccountResponse> createAccounts(String... usernames) {
        List<AccountResponse> accountResponses = new ArrayList<>();
        for (String username : usernames) {
            accountResponses.add(createAccount(username));
        }
        return accountResponses;
    }

    private AccountResponse createAccount(String username) {
        return accountService.create(AccountRequest.builder().username(username).build());
    }
}
