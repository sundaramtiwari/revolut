package com.revolut.domain;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static java.lang.Boolean.TRUE;
import static java.util.Objects.requireNonNull;

public class User {
    private final UUID id;
    private final String name;
    private final String email;
    private final ZonedDateTime create_on;

    private Boolean active;
    private Set<Account> accountSet;

    public User(String name, String email) {
        this.name = requireNonNull(name);
        this.email = requireNonNull(email);
        this.id = UUID.randomUUID();
        this.create_on = ZonedDateTime.now();
        this.active = TRUE;
        this.accountSet = new HashSet<>();
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public ZonedDateTime getCreate_on() {
        return create_on;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Account> getAccountSet() {
        return accountSet;
    }

    public void setAccountSet(Set<Account> accountSet) {
        this.accountSet = accountSet;
    }

    @Override

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
