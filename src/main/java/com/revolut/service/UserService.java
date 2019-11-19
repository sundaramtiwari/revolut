package com.revolut.service;

import com.revolut.domain.User;
import com.revolut.exception.UserException;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    public static Map<String, User> EMAIL_USER_CACHE = new HashMap<>();

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
