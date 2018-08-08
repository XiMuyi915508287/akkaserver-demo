package com.ximuyi.akkagame.core.scene.geography;

import com.ximuyi.akkagame.core.scene.PosXYZ;

public interface IPixConvert {
    PosXYZ transform(PixXYZ dotXYZ);

    PixXYZ transform(PosXYZ posXYZ);
}
