package com.ximuyi.game.core.sceneobject.component;

import com.ximuyi.game.core.sceneobject.ISceneObject;

public interface IObjetComponent {
    ISceneObject getObject();
    void setObject(ISceneObject object);
    ComponentType getType();
}
