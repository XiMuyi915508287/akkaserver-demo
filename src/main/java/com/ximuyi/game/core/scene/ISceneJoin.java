package com.ximuyi.game.core.scene;

import com.ximuyi.game.core.scene.object.ISceneObject;

@FunctionalInterface
public interface ISceneJoin {
    void onJoin(ISceneObject object, ISceneGrid toGrid);
}
