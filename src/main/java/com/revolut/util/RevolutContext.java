package com.revolut.util;

import com.revolut.service.AccountService;
import com.revolut.service.TransactionService;
import com.revolut.service.UserService;

public class RevolutContext {

    private static final UserService userService = new UserService();
    private static final AccountService accountService = new AccountService();
    private static final TransactionService tranService = new TransactionService();

    public static UserService getUserServiceInstance() {
        return userService;
    }

    public static AccountService getAccountServiceInstance() {
        return accountService;
    }

    public static TransactionService getTransactionServiceInstance() {
        return tranService;
    }

}
