package com.ximuyi.game.core.scene;

import com.ximuyi.game.core.sceneobject.ISceneObject;

@FunctionalInterface
public interface ISceneMove {
    void onMoveTo(ISceneObject object, ISceneGrid leaveGrid, ISceneGrid toGrid);
}
