package com.revolut.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

import static com.revolut.domain.Transaction.TransactionStatus.CREATED;
import static java.util.Objects.requireNonNull;

public class Transaction {

    private final UUID id;
    private final Account fromAccount;
    private final Account toAccount;
    private final BigDecimal amount;
    private final Currency currency;
    private final ZonedDateTime zonedDateTime;

    private TransactionStatus status;

    @JsonCreator
    public Transaction(Account fromAccount, Account toAccount, BigDecimal amount) {
        this.fromAccount = requireNonNull(fromAccount);
        this.toAccount = requireNonNull(toAccount);
        this.amount = requireNonNull(amount);
        this.currency = fromAccount.getCurrency();
        this.id = UUID.randomUUID();
        this.status = CREATED;
        zonedDateTime = ZonedDateTime.now();
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", amount=" + amount +
                ", currency=" + currency +
                ", zonedDateTime=" + zonedDateTime +
                ", status=" + status +
                '}';
    }

    public enum TransactionStatus {
        CREATED, IN_PROGRESS, SUCCESS, FAILED;
    }
}
