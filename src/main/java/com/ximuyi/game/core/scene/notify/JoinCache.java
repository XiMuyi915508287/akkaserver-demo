package com.ximuyi.game.core.scene.notify;

import com.ximuyi.game.core.scene.PosXYZ;
import com.ximuyi.game.core.scene.object.ISceneObject;
import com.ximuyi.game.core.scene.object.ObjectType;
import com.ximuyi.game.server.proto.ProtoScene;

public class JoinCache extends NotifyCache<ProtoScene.ProJoinScene> {

    private final ObjectType type;
    private final long uniqueId;
    private final PosXYZ posXYZ;

    public JoinCache(ISceneObject object) {
        this.type = object.getType();
        this.uniqueId = object.getUniqueId();
        this.posXYZ = object.getPos();
    }

    @Override
    protected ProtoScene.ProJoinScene serialize(int index) {
        ProtoScene.ProJoinScene.Builder builder = ProtoScene.ProJoinScene.newBuilder();
        return builder.setUniqueId(uniqueId).setType(type.defineId)
                .setPos(posXYZ.serialize()).build();
    }
}
