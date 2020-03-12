package com.ximuyi.game.core.sceneobject;

import com.ximuyi.game.common.util.Args;
import com.ximuyi.game.core.scene.IScene;
import com.ximuyi.game.core.scene.ISceneSchedule;
import com.ximuyi.game.core.scene.PosXYZ;
import com.ximuyi.game.core.sceneobject.component.ComponentType;
import com.ximuyi.game.core.sceneobject.component.IObjetComponent;

public interface ISceneObject extends ISceneSchedule {
    long getUniqueId();
    PosXYZ getPos();
    IScene getScene();
    void setPos(IScene scene, PosXYZ posXYZ);
    int trapWeight();
    ObjectType getType();
    Args.Two<IScene, PosXYZ> getLocation();
    <T extends IObjetComponent> T getComponent(ComponentType type);
    void addComponent(IObjetComponent component);
    <T extends IObjetComponent> T removeComponent(ComponentType type);
}
