package com.ximuyi.game.core.scene.notify;

import com.ximuyi.game.core.scene.PosXYZ;
import com.ximuyi.game.core.scene.object.ISceneObject;
import com.ximuyi.game.server.proto.ProtoScene;

class MoveCache extends NotifyCache<ProtoScene.ProMoveScene> {

    private final long uniqueId;
    private final PosXYZ posXYZ;

    public MoveCache(ISceneObject object) {
        this.uniqueId = object.getUniqueId();
        this.posXYZ = object.getPos();
    }

    @Override
    protected ProtoScene.ProMoveScene serialize(int index) {
        ProtoScene.ProMoveScene.Builder builder =  ProtoScene.ProMoveScene.newBuilder();
        builder.setUniqueId(uniqueId);
        builder.setPos(posXYZ.serialize());
        return builder.setIndex(index).build();
    }
}
