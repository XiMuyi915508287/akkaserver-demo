package com.ximuyi.game.core.sceneobject.component;

import com.ximuyi.game.core.sceneobject.ISceneObject;

public class ObjetComponent implements IObjetComponent {

    private final ComponentType type;
    private ISceneObject object;

    public ObjetComponent(ComponentType type) {
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
