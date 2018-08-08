package com.ximuyi.akkagame.core.sceneobject;

import com.ximuyi.akkagame.common.util.DoubleEntry;
import com.ximuyi.akkagame.core.scene.IScene;
import com.ximuyi.akkagame.core.scene.ISceneSchedule;
import com.ximuyi.akkagame.core.scene.PosXYZ;
import com.ximuyi.akkagame.core.sceneobject.component.ComponentType;
import com.ximuyi.akkagame.core.sceneobject.component.IObjetComponent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SceneObject implements ISceneObject {

    private final long uniqueId;
    private DoubleEntry<IScene, PosXYZ> location;
    private Map<ComponentType, IObjetComponent> components;

    public SceneObject(long uniqueId) {
        this.uniqueId = uniqueId;
        this.location = new DoubleEntry<>(null, null);
        this.components = new ConcurrentHashMap<>(ComponentType.values().length);
    }

    @Override
    public long getUniqueId() {
        return uniqueId;
    }

    @Override
    public PosXYZ getPos() {
        return location.second;
    }

    @Override
    public IScene getScene() {
        return location.first;
    }

    @Override
    public void setPos(IScene scene, PosXYZ posXYZ) {
        this.location = new DoubleEntry<>(scene, posXYZ);
    }

    @Override
    public int trapWeight() {
        return 0;
    }

    @Override
    public DoubleEntry<IScene, PosXYZ> getLocation() {
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
