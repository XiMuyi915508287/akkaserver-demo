package com.ximuyi.akkagame.core.sceneobject.component;

import com.ximuyi.akkagame.core.sceneobject.ISceneObject;

public interface IObjetComponent {
    ISceneObject getObject();
    void setObject(ISceneObject object);
    ComponentType getType();
}
