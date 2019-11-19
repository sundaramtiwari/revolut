package com.revolut.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static java.util.Currency.getInstance;
import static java.util.Objects.requireNonNull;

public class Account {
    @JsonProperty
    private final UUID id;
    @JsonProperty
    private final Currency currency;
    @JsonProperty
    private BigDecimal balance;

    @JsonCreator
    public Account(BigDecimal balance, Locale locale) {
        this.balance = requireNonNull(balance);
        this.currency = getInstance(requireNonNull(locale));
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

    @JsonProperty
    public UUID getId() {
        return id;
    }

    @JsonProperty
    public BigDecimal getBalance() {
        return balance;
    }

    @JsonProperty
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @JsonProperty
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