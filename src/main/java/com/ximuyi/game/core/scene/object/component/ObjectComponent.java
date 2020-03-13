package com.ximuyi.game.core.scene.object.component;

import com.ximuyi.game.core.scene.object.ISceneObject;

public class ObjectComponent implements IObjectComponent {

    private final ComponentType type;
    private ISceneObject object;

    public ObjectComponent(ComponentType type) {
        this.type = type;
    }

    public void setObject(ISceneObject object){
        this.object = object;
    }

    @Override
    public ISceneObject getObject() {
        return object;
    }

    @Override
    public ComponentType getType() {
        return type;
    }
}
