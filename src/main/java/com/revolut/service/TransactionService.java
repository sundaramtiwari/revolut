package com.revolut.service;

import com.revolut.domain.Account;
import com.revolut.domain.Transaction;
import com.revolut.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.revolut.domain.Transaction.TransactionStatus.FAILED;
import static com.revolut.domain.Transaction.TransactionStatus.IN_PROGRESS;
import static com.revolut.domain.Transaction.TransactionStatus.SUCCESS;
import static com.revolut.service.AccountService.ACCOUNT_MAP;

public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    public Transaction initiateTransfer(String senderId, String receiverId, String amountStr) {
        logger.info(String.format("Initiating transfer from: " + senderId + " to " + receiverId + " for amount: " + amountStr));
        BigDecimal amount;

        try {
            amount = BigDecimal.valueOf(Double.parseDouble(amountStr));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid amount requested to transfer: " + amountStr, e);
        }

        return initiateTransfer(ACCOUNT_MAP.get(UUID.fromString(senderId)),
                ACCOUNT_MAP.get(UUID.fromString(receiverId)), amount);
    }

    private Transaction initiateTransfer(Account senderAccount, Account recipientAccount, BigDecimal amount) {
        if (Objects.isNull(senderAccount) || Objects.isNull(recipientAccount))
            throw new TransactionException("Sender or Recepient not found");

        Transaction transaction = new Transaction(senderAccount, recipientAccount, amount);
        Lock writeLock = TransactionService.lock.writeLock();

        try {
            writeLock.lock();
            transaction.setStatus(IN_PROGRESS);

            if (amount.compareTo(senderAccount.getBalance()) < 1) {
                transferAmount(senderAccount, recipientAccount, amount, transaction);
                transaction.setStatus(SUCCESS);

            } else {
                transaction.setStatus(FAILED);
                throw new TransactionException("Insufficient balance in sender account: " + senderAccount.getBalance());
            }
        } finally {

            writeLock.unlock();
        }

        return transaction;
    }

    private void transferAmount(Account senderAccount, Account recipientAccount, BigDecimal amount, Transaction transaction) {
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        recipientAccount.setBalance(recipientAccount.getBalance().add(amount));
        transaction.setStatus(SUCCESS);
    }
}
