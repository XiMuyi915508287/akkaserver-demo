package com.ximuyi.game.core.scene;

import com.ximuyi.game.core.scene.object.ISceneObject;

@FunctionalInterface
public interface ISceneLeave {
    void onLeave(ISceneObject object, ISceneGrid toGrid);
}
