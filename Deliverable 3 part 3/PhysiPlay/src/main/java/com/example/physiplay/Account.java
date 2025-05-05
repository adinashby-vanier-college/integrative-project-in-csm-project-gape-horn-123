package com.example.physiplay;

import com.google.gson.annotations.Expose;

import java.util.Objects;

public class Account {
    @Expose
    public String name;
    @Expose
    public String password;

    public Account(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(name, account.name) && Objects.equals(password, account.password);
    }
}
