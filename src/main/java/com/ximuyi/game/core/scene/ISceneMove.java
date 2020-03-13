package com.ximuyi.game.core.scene;

import com.ximuyi.game.core.scene.object.ISceneObject;

@FunctionalInterface
public interface ISceneMove {
    void onMoveTo(ISceneObject object, ISceneGrid leaveGrid, ISceneGrid toGrid);
}
