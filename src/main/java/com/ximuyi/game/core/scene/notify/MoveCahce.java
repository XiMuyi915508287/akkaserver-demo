package com.ximuyi.game.core.scene.notify;

import com.ximuyi.game.core.scene.PosXYZ;
import com.ximuyi.game.core.sceneobject.ISceneObject;
import com.ximuyi.game.server.proto.ProtoScene;

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
