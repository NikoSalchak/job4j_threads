package ru.job4j.cash;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import ru.job4j.cash.exception.AmountBelowZeroException;

import java.util.HashMap;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {

    @GuardedBy("this")
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        return accounts.putIfAbsent(account.id(), account) == null;

    }

    public synchronized boolean update(Account account) {
        return accounts.put(account.id(), account) != null;
    }

    public synchronized void delete(int id) {
        accounts.remove(id);
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        boolean success = false;
        if (accounts.containsKey(fromId) && accounts.containsKey(toId)) {
            Account fromAccount = accounts.get(fromId);
            if (fromAccount.amount() - amount < 0) {
                throw new AmountBelowZeroException(String.format("the account with id %s does not have enough funds to transfer money", accounts.get(fromId).id()));
            }
            Account fromAccountNew = new Account(fromId, fromAccount.amount() - amount);
            accounts.put(fromId, fromAccountNew);
            Account toAccount = accounts.get(toId);
            Account accountToNew = new Account(toId, toAccount.amount() + amount);
            accounts.put(toId, accountToNew);
            success = true;
        }
        return success;
    }
}
