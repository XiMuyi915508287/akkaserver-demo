package com.ximuyi.akkagame.core.sceneobject;

import com.ximuyi.akkaserver.IUser;

public class ScenePlayer extends SceneObject implements IUser {

    private final IUser user;

    public ScenePlayer(IUser user, long uniqueId) {
        super(uniqueId);
        this.user = user;
    }

    @Override
    public int trapWeight() {
        return 10;
    }

    @Override
    public ObjectType getType() {
        return ObjectType.Player;
    }

    @Override
    public String getAccount() {
        return user.getAccount();
    }

    @Override
    public boolean isLogin() {
        return user.isLogin();
    }

    @Override
    public long getUserId() {
        return user.getUserId();
    }
}
