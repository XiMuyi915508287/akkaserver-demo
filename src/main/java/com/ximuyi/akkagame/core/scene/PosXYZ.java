package com.ximuyi.akkagame.core.scene;

import com.ximuyi.akkagame.server.coder.ICodeSerialize;
import com.ximuyi.akkagame.server.proto.ProtoScene;

public class PosXYZ implements ICodeSerialize<ProtoScene.ProPosXYZ> {
    public static final PosXYZ NONE = new PosXYZ(0,0,0);

    public final float x;
    public final float y;
    public final float z;

    public PosXYZ(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PosXYZ(float x, float y) {
       this(x, y, 0);
    }

    public boolean isNone(){
        return this == NONE;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public ProtoScene.ProPosXYZ serialize() {
        ProtoScene.ProPosXYZ.Builder builder = ProtoScene.ProPosXYZ.newBuilder();
        return builder.setX(x).setY(y).build();
    }
}
