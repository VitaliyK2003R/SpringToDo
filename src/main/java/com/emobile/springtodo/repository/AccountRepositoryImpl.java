package com.emobile.springtodo.repository;

import com.emobile.springtodo.exception.AccountNotFoundException;
import com.emobile.springtodo.model.Account;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {
    private final EntityManager entityManager;

    @Override
    public Account create(Account account) {
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        entityManager.getTransaction().commit();
        return account;
    }

    @Override
    public Account get(UUID accountId) {
        return getAccountWithEntityManager(accountId);
    }

    private Account getAccountWithEntityManager(UUID accountId) throws AccountNotFoundException {
        Account account = entityManager.find(Account.class, accountId);
        if (account == null) {
            throw new AccountNotFoundException(accountId);
        }
        return account;
    }

    @Override
    public List<Account> getAll() {
        return (List<Account>) entityManager.createQuery("select account from Account account").getResultList();
    }

    @Override
    public void update(UUID accountId, Account account) {
        try {
            entityManager.getTransaction().begin();
            updateAccount(accountId, account);
            entityManager.getTransaction().commit();
        } catch (AccountNotFoundException ex) {
            entityManager.getTransaction().rollback();
            throw ex;
        }
    }

    private void updateAccount(UUID accountId, Account updatedAccount) {
        Account updatableAccount = getAccountWithEntityManager(accountId);
        updatableAccount.setUsername(updatedAccount.getUsername());
        updatableAccount.setTasks(updatedAccount.getTasks());
        entityManager.persist(updatableAccount);
    }

    @Override
    public void delete(UUID accountId) {
        entityManager.getTransaction().begin();
        Account deletableAccount = getAccountWithEntityManager(accountId);
        entityManager.remove(deletableAccount);
        entityManager.getTransaction().commit();
    }
}
