package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.Account;

import java.util.List;
import java.util.UUID;

public interface AccountRepository {
    Account create(Account account);
    Account get(UUID accountId);
    List<Account> getAll();
    void update(UUID accountId, Account account);
    void delete(UUID accountId);
}
