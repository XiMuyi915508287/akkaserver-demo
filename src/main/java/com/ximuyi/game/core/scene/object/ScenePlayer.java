package com.ximuyi.game.core.scene.object;

import com.ximuyi.core.user.IUser;

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
    public long getUserId() {
        return user.getUserId();
    }
}
