package com.ximuyi.akkagame.core.scene;

import com.ximuyi.akkagame.core.sceneobject.ISceneObject;

@FunctionalInterface
public interface ISceneMove {
    void onMoveTo(ISceneObject object, ISceneGrid leaveGrid, ISceneGrid toGrid);
}
