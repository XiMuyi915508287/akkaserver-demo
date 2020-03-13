package com.ximuyi.game.core.scene.object.component;

import com.ximuyi.game.core.scene.object.ISceneObject;

public interface IObjectComponent {
    ISceneObject getObject();
    void setObject(ISceneObject object);
    ComponentType getType();
}
