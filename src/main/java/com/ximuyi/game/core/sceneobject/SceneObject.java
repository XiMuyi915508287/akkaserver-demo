package com.ximuyi.game.core.sceneobject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ximuyi.game.common.util.Args;
import com.ximuyi.game.core.scene.IScene;
import com.ximuyi.game.core.scene.ISceneSchedule;
import com.ximuyi.game.core.scene.PosXYZ;
import com.ximuyi.game.core.sceneobject.component.ComponentType;
import com.ximuyi.game.core.sceneobject.component.IObjetComponent;

public abstract class SceneObject implements ISceneObject {

    private final long uniqueId;
    private Args.Two<IScene, PosXYZ> location;
    private Map<ComponentType, IObjetComponent> components;

    public SceneObject(long uniqueId) {
        this.uniqueId = uniqueId;
        this.location = Args.create(null, null);
        this.components = new ConcurrentHashMap<>(ComponentType.values().length);
    }

    @Override
    public long getUniqueId() {
        return uniqueId;
    }

    @Override
    public PosXYZ getPos() {
        return location.arg1;
    }

    @Override
    public IScene getScene() {
        return location.arg0;
    }

    @Override
    public void setPos(IScene scene, PosXYZ posXYZ) {
        this.location = Args.create(scene, posXYZ);
    }

    @Override
    public int trapWeight() {
        return 0;
    }

    @Override
    public  Args.Two<IScene, PosXYZ> getLocation() {
        return location.clone();
    }

    @Override
    public <T extends IObjetComponent> T getComponent(ComponentType type) {
        IObjetComponent component = components.get(type);
        return component == null ? null : (T)component;
    }

    @Override
    public void addComponent(IObjetComponent component) {
        component.setObject(this);
        components.put(component.getType(), component);
    }

    @Override
    public <T extends IObjetComponent> T removeComponent(ComponentType type) {
        IObjetComponent component = components.remove(type);
        if (component != null){
            component.setObject(null);
            return (T)component;
        }
        else {
            return null;
        }
    }

    @Override
    public void onTick() {
        for (IObjetComponent component : components.values()){
            if (component instanceof ISceneSchedule){
                ((ISceneSchedule)component).onTick();
            }
        }
    }
}
