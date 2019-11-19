package com.revolut.service;

import com.revolut.domain.User;
import com.revolut.exception.UserException;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class UserService {

    public static TreeMap<String, User> EMAIL_USER_CACHE = new TreeMap<>();

    public User createUser(String name, String email) {
        if (!EMAIL_USER_CACHE.containsKey(email)) {
            User user = new User(name, email);
            EMAIL_USER_CACHE.put(email, user);
            return user;

        } else {
            throw new UserException("User already exists with email: " + email);
        }
    }
}
