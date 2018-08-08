package com.ximuyi.akkagame.core.scene;

import com.ximuyi.akkagame.common.util.DoubleEntry;
import com.ximuyi.akkagame.common.util.ResultCode;
import com.ximuyi.akkagame.core.scene.geography.ISceneTerrain;
import com.ximuyi.akkagame.core.scene.geography.PixXYZ;
import com.ximuyi.akkagame.core.scene.notify.ISceneNotify;
import com.ximuyi.akkagame.core.scene.notify.SceneNotify;
import com.ximuyi.akkagame.core.sceneobject.ISceneObject;
import com.ximuyi.akkagame.core.sceneobject.ObjectType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Scene implements IScene, ISceneDispose{
    private static final Logger logger = LoggerFactory.getLogger(Scene.class);
    private static final List<ObjectType> Priority = Arrays.asList(ObjectType.values()).stream().filter( o-> o.orderTick > 0).collect(Collectors.toList());
    static {
        Priority.sort(Comparator.comparingInt(o -> o.orderTick));
    }
    private final long uniqueId;
    private final int defineId;
    private final SceneOwn sceneOwn;
    private final ISceneTerrain terrain;
    private final ISceneNotify notify;

    public Scene(long uniqueId, int defineId, SceneOwn sceneOwn, ISceneTerrain terrain) {
        this.uniqueId = uniqueId;
        this.defineId = defineId;
        this.sceneOwn = sceneOwn;
        this.terrain = terrain;
        this.notify = new SceneNotify(this);
    }

    @Override
    public long getUniqueId() {
        return uniqueId;
    }

    @Override
    public int getDefineId() {
        return defineId;
    }

    @Override
    public SceneOwn getSceneOwn() {
        return sceneOwn;
    }

    @Override
    public int objectNum() {
        final int[] count = {0};
        forGrid( grid-> count[0] += grid.objectNum(), null);
        return count[0];
    }

    @Override
    public <T extends ISceneObject> Collection<T> objects(ObjectType type){
        List<T> objects = new ArrayList<>();
        forGrid(grid -> objects.addAll(grid.objects(type)), null);
        return objects;
    }

    @Override
    public ResultCode join(ISceneObject object, PosXYZ posXYZ) {
        PixXYZ pixXYZ = terrain.transform(posXYZ);
        if (!terrain.canWalk(pixXYZ)){
            return ResultCode.SCENE_INVALID_POS;
        }
        ISceneGrid sceneGrid = terrain.get(pixXYZ);
        if (sceneGrid == null){
            return ResultCode.SCENE_INVALID_GRID;
        }
        DoubleEntry<IScene, PosXYZ> entry = object.getLocation();
        if (entry.first == null){
            //没有场景
            return tryJoin(object, sceneGrid, posXYZ);
        }
        else if (entry.first.getUniqueId() == this.getUniqueId()){
            //同一个场景
            return tryMove(object,entry.second, sceneGrid, pixXYZ, posXYZ);
        }
        else {
            //在其他场景，先离开场景在进入
            ResultCode resultCode = entry.first.leave(object);
            if (resultCode.isSuccess()){
                resultCode = tryJoin(object, sceneGrid, posXYZ);
            }
            return resultCode;
        }
    }

    @Override
    public ResultCode leave(ISceneObject object) {
        DoubleEntry<IScene, PosXYZ> entry = object.getLocation();
        if (entry.first == null){
            return ResultCode.SCENE_NOT_IN_GRID;
        }
        if (entry.first.getUniqueId() != this.getUniqueId()){
            return ResultCode.SCENE_NOT_IN_SAME;
        }
        PixXYZ sceneXYZ = terrain.transform(entry.second);
        ISceneGrid sceneGrid = terrain.get(sceneXYZ);
        return tryLeave(object, sceneGrid);
    }

    @Override
    public ResultCode move(ISceneObject object, PosXYZ posXYZ) {
        DoubleEntry<IScene, PosXYZ> entry = object.getLocation();
        if (entry.first == null){
            return ResultCode.SCENE_NOT_IN_GRID;
        }
        if (entry.first.getUniqueId() != this.getUniqueId()){
            return ResultCode.SCENE_NOT_IN_SAME;
        }
        PixXYZ newPixXYZ = terrain.transform(posXYZ);
        if (!terrain.canWalk(newPixXYZ)){
            return ResultCode.SCENE_INVALID_POS;
        }
        ISceneGrid newSceneGrid = terrain.get(newPixXYZ);
        if (newSceneGrid == null){
            return ResultCode.SCENE_INVALID_GRID;
        }
        return tryMove(object, entry.second, newSceneGrid, newPixXYZ, posXYZ);
    }

    public void forGrid(Consumer<ISceneGrid> consumer, Predicate<ISceneGrid> predicate){
        terrain.forGrid(consumer, predicate);
    }

    @Override
    public ISceneTerrain getTerrain() {
        return terrain;
    }

    @Override
    public void onTick() {
        for (ObjectType type : Priority) {
            Collection<ISceneObject> objects = objects(type);
            for (ISceneObject object : objects) {
                try {
                    //遇到一个Bug：object 从前面的网格执行AI 进入 后面的网格，那么后面的网格还会OnTick一次
                    // 所以先把所有object取出来在执行
                    object.onTick();
                }
                catch (Throwable t){
                    logger.error("scene object[{}] ontick error.", object.getUniqueId(), t);
                }
            }
        }
    }

    @Override
    public void dispose() {
        notify.dispose();
    }

    /**
     * @param object
     * @param sceneGrid posXYZ 对应的网格区域
     * @param posXYZ
     * @return
     */
    private ResultCode tryJoin(ISceneObject object, ISceneGrid sceneGrid, PosXYZ posXYZ){
        if (sceneGrid.add(object)){
            object.setPos(this, posXYZ);
            notify.onJoin(object, sceneGrid);
            return ResultCode.SUCCESS;
        }
        else {
            error("is already in ", object, sceneGrid);
            return ResultCode.SERVER_ERROR;
        }
    }


    /**
     * @param object
     * @param sceneGrid
     * @return
     */
    private ResultCode tryLeave(ISceneObject object, ISceneGrid sceneGrid){
        if (sceneGrid.remove(object)) {
            object.setPos(null, null);
            notify.onLeave(object,  sceneGrid);
            return ResultCode.SUCCESS;
        }
        else {
            error("wasn't in", object, sceneGrid);
            return ResultCode.SERVER_ERROR;
        }
    }

    /**
     *
     * @param object
     * @param newSceneGrid
     * @param oldPoxXYZ
     * @param pixXYZ
     * @param posXYZ
     * @return
     */
    private ResultCode tryMove(ISceneObject object, PosXYZ oldPoxXYZ, ISceneGrid newSceneGrid, PixXYZ pixXYZ, PosXYZ posXYZ){
        PixXYZ oldPixXYZ = terrain.transform(oldPoxXYZ);
        //需要检测两个点之间是否可以连通
        ISceneGrid oldSceneGrid = terrain.get(oldPixXYZ);
        if (oldSceneGrid != newSceneGrid){
            if (Math.abs(oldSceneGrid.getGridX() - newSceneGrid.getGridX()) > 1){
                return ResultCode.SCENE_MOVE_FAR;
            }
            if (Math.abs(oldSceneGrid.getGridY() - newSceneGrid.getGridY()) > 1){
                return ResultCode.SCENE_MOVE_FAR;
            }
            if (oldSceneGrid.remove(object)) {
                if (newSceneGrid.add(object)) {
                }
                else {
                    error("is already in ", object, newSceneGrid);
                    return ResultCode.SERVER_ERROR;
                }
            }
            else {
                error("wasn't in", object, oldSceneGrid);
                return ResultCode.SERVER_ERROR;
            }
        }
        object.setPos(this, posXYZ);
        notify.onMoveTo(object, oldSceneGrid, newSceneGrid);
        return ResultCode.SUCCESS;
    }

    /**
     *
     * @param message
     * @param object
     * @param sceneGrid
     */
    private void error(String message, ISceneObject object, ISceneGrid sceneGrid){
        logger.error("[{}-{}] {} {}-{}-{},{}", object.getClass().getName(), object.getUniqueId(), message,
                     getDefineId(), getUniqueId(), sceneGrid.getGridX(), sceneGrid.getGridY());
    }

}
