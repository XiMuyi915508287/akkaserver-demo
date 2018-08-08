package com.ximuyi.akkagame.core.scene.notify;

import com.ximuyi.akkagame.core.scene.PosXYZ;
import com.ximuyi.akkagame.core.sceneobject.ISceneObject;
import com.ximuyi.akkagame.server.proto.ProtoScene;

class MoveCahce extends NotifyCahce<ProtoScene.ProMoveScene> {

    private final long uniqueId;
    private final PosXYZ posXYZ;

    public MoveCahce(ISceneObject object) {
        this.uniqueId = object.getUniqueId();
        this.posXYZ = object.getPos();
    }

    @Override
    protected ProtoScene.ProMoveScene serialize(int inex) {
        ProtoScene.ProMoveScene.Builder builder =  ProtoScene.ProMoveScene.newBuilder();
        builder.setUniqueId(uniqueId);
        builder.setPos(posXYZ.serialize());
        return builder.setIndex(inex).build();
    }
}
