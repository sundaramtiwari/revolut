package com.revolut.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;
import static java.util.Currency.getInstance;
import static java.util.Objects.requireNonNull;

public class Account {
    private UUID id;
    private Currency currency;
    private BigDecimal balance;

    @JsonCreator(mode = PROPERTIES)
    public Account(@JsonProperty("id") String id,
                   @JsonProperty("currency") String currency,
                   @JsonProperty("balance") BigDecimal balance) {
        this.balance = requireNonNull(balance);
        this.currency = getInstance(requireNonNull(currency));
        this.id = requireNonNull(UUID.fromString(id));
    }

    public Account(BigDecimal balance, Currency currency) {
        this.balance = requireNonNull(balance);
        this.currency = requireNonNull(currency);
        this.id = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", currency=" + currency +
                ", balance=" + balance +
                '}';
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return id.equals(account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}