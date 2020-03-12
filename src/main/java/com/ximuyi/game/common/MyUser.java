package com.ximuyi.game.common;

import com.ximuyi.core.user.IUser;

public class MyUser implements IUser {

    private final long userId;
    private final String account;

    public MyUser(long userId, String account) {
        this.userId = userId;
        this.account = account;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public long getUserId() {
        return userId;
    }
}
