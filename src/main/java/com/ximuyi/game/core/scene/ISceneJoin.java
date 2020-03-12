package com.ximuyi.game.core.scene;

import com.ximuyi.game.core.sceneobject.ISceneObject;

@FunctionalInterface
public interface ISceneJoin {
    void onJoin(ISceneObject object, ISceneGrid toGrid);
}
