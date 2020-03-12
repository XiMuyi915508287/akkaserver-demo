package com.ximuyi.game.core.scene;

import com.ximuyi.game.core.sceneobject.ISceneObject;

@FunctionalInterface
public interface ISceneLeave {
    void onLeave(ISceneObject object, ISceneGrid toGrid);
}
