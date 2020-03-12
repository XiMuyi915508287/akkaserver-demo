package com.ximuyi.game.core.scene.geography;

import com.ximuyi.game.core.scene.PosXYZ;

public interface IPixConvert {
    PosXYZ transform(PixXYZ dotXYZ);

    PixXYZ transform(PosXYZ posXYZ);
}
