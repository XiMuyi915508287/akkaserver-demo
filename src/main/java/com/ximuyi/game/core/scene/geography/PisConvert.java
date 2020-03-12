package com.ximuyi.game.core.scene.geography;

import com.ximuyi.game.core.scene.PosXYZ;

public class PisConvert implements IPixConvert {

    @Override
    public PosXYZ transform(PixXYZ sceneXYZ) {
        return new PosXYZ(sceneXYZ.x, sceneXYZ.y, sceneXYZ.z);
    }

    @Override
    public PixXYZ transform(PosXYZ posXYZ) {
        return new PixXYZ((int)posXYZ.x, (int)posXYZ.y, (int)posXYZ.z);
    }
}
