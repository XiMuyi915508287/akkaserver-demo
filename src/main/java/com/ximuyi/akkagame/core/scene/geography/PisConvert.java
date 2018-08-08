package com.ximuyi.akkagame.core.scene.geography;

import com.ximuyi.akkagame.core.scene.PosXYZ;

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
