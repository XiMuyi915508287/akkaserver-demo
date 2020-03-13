package com.ximuyi.game.core.scene.notify;

import com.ximuyi.game.core.scene.object.ISceneObject;
import com.ximuyi.game.server.proto.ProtoScene;

class LeaveCache extends NotifyCache<ProtoScene.ProLeaveScene> {

    private final long uniqueId;

    public LeaveCache(ISceneObject object) {
        this.uniqueId = object.getUniqueId();
    }

    @Override
    protected ProtoScene.ProLeaveScene serialize(int index) {
        ProtoScene.ProLeaveScene.Builder builder = ProtoScene.ProLeaveScene.newBuilder();
        return builder.setUniqueId(uniqueId).setIndex(index).build();
    }
}
