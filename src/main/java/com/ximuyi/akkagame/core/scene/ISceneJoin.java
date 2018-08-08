package com.ximuyi.akkagame.core.scene;

import com.ximuyi.akkagame.core.sceneobject.ISceneObject;

@FunctionalInterface
public interface ISceneJoin {
    void onJoin(ISceneObject object, ISceneGrid toGrid);
}
