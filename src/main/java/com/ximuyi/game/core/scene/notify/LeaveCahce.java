package com.ximuyi.game.core.scene.notify;

import com.ximuyi.game.core.sceneobject.ISceneObject;
import com.ximuyi.game.server.proto.ProtoScene;

class LeaveCahce extends NotifyCahce<ProtoScene.ProLeaveScene>{

    private final long uniqueId;

    public LeaveCahce(ISceneObject object) {
        this.uniqueId = object.getUniqueId();
    }

    @Override
    protected ProtoScene.ProLeaveScene serialize(int inex) {
        ProtoScene.ProLeaveScene.Builder builder = ProtoScene.ProLeaveScene.newBuilder();
        return builder.setUniqueId(uniqueId).setIndex(inex).build();
    }
}
