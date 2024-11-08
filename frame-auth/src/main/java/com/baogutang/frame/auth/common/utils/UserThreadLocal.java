package com.baogutang.frame.auth.common.utils;

import com.baogutang.frame.auth.model.entity.User;

/**
 * @author N1KO
 */
public class UserThreadLocal {

    private static final ThreadLocal<User> userThread = new ThreadLocal<>();

    public static void set(User user) {
        userThread.set(user);
    }

    public static User get() {
        return userThread.get();
    }

    public static void remove() {
        userThread.remove();
    }

}
