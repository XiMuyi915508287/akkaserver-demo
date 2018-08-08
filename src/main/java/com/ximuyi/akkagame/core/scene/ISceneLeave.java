package com.ximuyi.akkagame.core.scene;

import com.ximuyi.akkagame.core.sceneobject.ISceneObject;

@FunctionalInterface
public interface ISceneLeave {
    void onLeave(ISceneObject object, ISceneGrid toGrid);
}
