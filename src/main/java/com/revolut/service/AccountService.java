package com.revolut.service;

import com.revolut.domain.Account;
import com.revolut.domain.User;
import com.revolut.exception.UserException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class AccountService {

    public static Map<UUID, Account> ACCOUNT_MAP = new HashMap<>();

    public Account createAccount(User user, BigDecimal balance) {
        return createAccount(user, balance, Currency.getInstance(Locale.GERMANY));
    }

    public Account createAccount(User user, BigDecimal balance, Currency currency) {
        Account account = new Account(balance, currency);
        User existingUser = UserService.EMAIL_USER_CACHE.get(user.getEmail());

        if (Objects.isNull(existingUser))
            throw new UserException("No user exists for email: " + user.getEmail());

        Set<Account> accounts = existingUser.getAccountSet();

        accounts.add(account);
        ACCOUNT_MAP.put(account.getId(), account);
        return account;
    }
}
