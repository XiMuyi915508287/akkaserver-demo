package com.ximuyi.game.core.scene;

import com.ximuyi.game.core.sceneobject.ISceneObject;
import com.ximuyi.game.core.sceneobject.ObjectType;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SceneGrid implements ISceneGrid {

    private final int x;
    private final int y;
    private final long uniqueId;
    private final Map<Long, ISceneObject>[] maps;

    public SceneGrid(int x, int y) {
        this.x = x;
        this.y = y;
        this.uniqueId = ((long)x) << 32 | y;
        ObjectType[] types = ObjectType.values();
        this.maps = new Map[types.length];
        for (ObjectType type : types) {
            this.maps[type.ordinal()] = new ConcurrentHashMap<>();
        }
    }

    @Override
    public int getGridX() {
        return x;
    }

    @Override
    public int getGridY() {
        return y;
    }

    @Override
    public long getUniqueId() {
        return uniqueId;
    }

    @Override
    public boolean add(ISceneObject object) {
        return map(object.getType()).put(object.getUniqueId(), object) == null;
    }

    @Override
    public boolean remove(ISceneObject object) {
        return map(object.getType()).remove(object.getUniqueId()) != null;
    }

    @Override
    public <T extends ISceneObject> Collection<T> objects(ObjectType type) {
        return (Collection<T>)map(type).values();
    }

    @Override
    public int objectNum() {
        int count = 0;
        for (Map<Long, ISceneObject> map : maps) {
            count += map.size();
        }
        return count;
    }

    private Map<Long, ISceneObject> map(ObjectType type){
        return maps[type.ordinal()];
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                ", size=" + objectNum() +
                '}';
    }
}
