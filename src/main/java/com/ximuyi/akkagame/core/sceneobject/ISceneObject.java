package com.ximuyi.akkagame.core.sceneobject;

import com.ximuyi.akkagame.common.util.DoubleEntry;
import com.ximuyi.akkagame.core.scene.IScene;
import com.ximuyi.akkagame.core.scene.ISceneSchedule;
import com.ximuyi.akkagame.core.scene.PosXYZ;
import com.ximuyi.akkagame.core.sceneobject.component.ComponentType;
import com.ximuyi.akkagame.core.sceneobject.component.IObjetComponent;

public interface ISceneObject extends ISceneSchedule {
    long getUniqueId();
    PosXYZ getPos();
    IScene getScene();
    void setPos(IScene scene, PosXYZ posXYZ);
    int trapWeight();
    ObjectType getType();
    DoubleEntry<IScene, PosXYZ> getLocation();
    <T extends IObjetComponent> T getComponent(ComponentType type);
    void addComponent(IObjetComponent component);
    <T extends IObjetComponent> T removeComponent(ComponentType type);
}
