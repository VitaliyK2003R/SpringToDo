package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.request.AccountRequest;
import com.emobile.springtodo.dto.response.AccountResponse;
import com.emobile.springtodo.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ActiveProfiles("test")
public class AccountControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;

    @Test
    public void successCreatingAccountTest() throws Exception {
        AccountRequest accountRequest = AccountRequest.builder().username("account").build();
        UUID accountId = getUUID(18);
        AccountResponse expectedAccountResponse = AccountResponse.builder()
                .id(accountId)
                .username("account")
                .build();

        when(accountService.create(accountRequest)).thenReturn(expectedAccountResponse);

        mockMvc.perform(post("/api/v1/accounts")
                        .content(objectMapper.writeValueAsString(accountRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(accountId.toString()))
                .andExpect(jsonPath("$.username").value("account"));
    }

    @Test
    public void successGettingAccountTest() throws Exception {
        UUID accountId = getUUID(10);
        AccountResponse expectedAccountResponse = AccountResponse.builder().id(accountId).username("account").build();

        when(accountService.get(accountId)).thenReturn(expectedAccountResponse);

        mockMvc.perform(get("/api/v1/accounts/{id}", accountId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(accountId.toString()))
                .andExpect(jsonPath("$.username").value("account"));
    }

    @Test
    public void successGettingAllTest() throws Exception {
        List<AccountResponse> expectedAccountResponses = createAccountResponses("account1", "account2");

        when(accountService.getAll()).thenReturn(expectedAccountResponses);

        mockMvc.perform(get("/api/v1/accounts"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedAccountResponses)));
    }

    @Test
    public void successUpdatingTest() throws Exception {
        UUID accountId = getUUID(13);
        AccountRequest accountRequest = createAccountRequest("updatedAccount");

        doNothing().when(accountService).update(accountId, accountRequest);

        mockMvc.perform(put("/api/v1/accounts/{id}", accountId)
                        .content(objectMapper.writeValueAsString(accountRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void successDeletingTest() throws Exception {
        UUID accountId = getUUID(123);

        doNothing().when(accountService).delete(accountId);

        mockMvc.perform(delete("/api/v1/accounts/{id}", accountId))
                .andExpect(status().is2xxSuccessful());
    }

    private UUID getUUID(Integer number) {
        if (number < 0) {
            throw new IllegalArgumentException("Number have to be better than 0 or equals it");
        }
        return UUID.fromString("0000000-0000-0000-0000-" + number);
    }

    private AccountRequest createAccountRequest(String username) {
        return AccountRequest.builder().username(username).build();
    }

    private AccountResponse createAccountResponse(String username) {
        return AccountResponse.builder().username(username).build();
    }

    private List<AccountResponse> createAccountResponses(String... usernames) {
        List<AccountResponse> accountResponses = new ArrayList<>();
        for (String username : usernames) {
            accountResponses.add(createAccountResponse(username));
        }
        return accountResponses;
    }
}
