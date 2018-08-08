package com.ximuyi.akkagame.core.scene.notify;

import com.ximuyi.akkagame.core.sceneobject.ISceneObject;
import com.ximuyi.akkagame.server.proto.ProtoScene;

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
