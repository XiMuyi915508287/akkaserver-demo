package com.ximuyi.game.core.scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.ximuyi.core.coder.ResultCode;
import com.ximuyi.game.common.util.Args;
import com.ximuyi.game.common.util.MyResultCode;
import com.ximuyi.game.core.scene.geography.ISceneTerrain;
import com.ximuyi.game.core.scene.geography.PixXYZ;
import com.ximuyi.game.core.scene.notify.ISceneNotify;
import com.ximuyi.game.core.scene.notify.SceneNotify;
import com.ximuyi.game.core.scene.object.ISceneObject;
import com.ximuyi.game.core.scene.object.ObjectType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scene implements IScene, ISceneDispose{
    private static final Logger logger = LoggerFactory.getLogger(Scene.class);

    private static final List<ObjectType> priorities = Arrays.stream(ObjectType.values())
            .filter(ObjectType::isScheduled)
            .sorted(Comparator.comparingInt(ObjectType::getPriority))
            .collect(Collectors.toList());;

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
            return MyResultCode.SCENE_INVALID_POS;
        }
        ISceneGrid sceneGrid = terrain.get(pixXYZ);
        if (sceneGrid == null){
            return MyResultCode.SCENE_INVALID_GRID;
        }
        Args.Two<IScene, PosXYZ> entry = object.getLocation();
        if (entry.arg0 == null){
            //没有场景
            return tryJoin(object, sceneGrid, posXYZ);
        }
        else if (entry.arg0.getUniqueId() == this.getUniqueId()){
            //同一个场景
            return tryMove(object,entry.arg1, sceneGrid, pixXYZ, posXYZ);
        }
        else {
            //在其他场景，先离开场景在进入
            ResultCode resultCode = entry.arg0.leave(object);
            if (resultCode.isSuccess()){
                resultCode = tryJoin(object, sceneGrid, posXYZ);
            }
            return resultCode;
        }
    }

    @Override
    public ResultCode leave(ISceneObject object) {
        Args.Two<IScene, PosXYZ> entry = object.getLocation();
        if (entry.arg0 == null){
            return MyResultCode.SCENE_NOT_IN_GRID;
        }
        if (entry.arg0.getUniqueId() != this.getUniqueId()){
            return MyResultCode.SCENE_NOT_IN_SAME;
        }
        PixXYZ sceneXYZ = terrain.transform(entry.arg1);
        ISceneGrid sceneGrid = terrain.get(sceneXYZ);
        return tryLeave(object, sceneGrid);
    }

    @Override
    public ResultCode move(ISceneObject object, PosXYZ posXYZ) {
        Args.Two<IScene, PosXYZ> entry = object.getLocation();
        if (entry.arg0 == null){
            return MyResultCode.SCENE_NOT_IN_GRID;
        }
        if (entry.arg0.getUniqueId() != this.getUniqueId()){
            return MyResultCode.SCENE_NOT_IN_SAME;
        }
        PixXYZ newPixXYZ = terrain.transform(posXYZ);
        if (!terrain.canWalk(newPixXYZ)){
            return MyResultCode.SCENE_INVALID_POS;
        }
        ISceneGrid newSceneGrid = terrain.get(newPixXYZ);
        if (newSceneGrid == null){
            return MyResultCode.SCENE_INVALID_GRID;
        }
        return tryMove(object, entry.arg1, newSceneGrid, newPixXYZ, posXYZ);
    }

    public void forGrid(Consumer<ISceneGrid> consumer, Predicate<ISceneGrid> predicate){
        terrain.forGrid(consumer, predicate);
    }

    @Override
    public ISceneTerrain getTerrain() {
        return terrain;
    }

    @Override
    public void onScheduled() {
        for (ObjectType type : priorities) {
            Collection<ISceneObject> objects = objects(type);
            for (ISceneObject object : objects) {
                try {
                    //遇到一个Bug：object 从前面的网格执行AI 进入 后面的网格，那么后面的网格还会OnTick一次
                    // 所以先把所有object取出来在执行
                    object.onScheduled();
                }
                catch (Throwable t){
                    logger.error("scene object[{}] onScheduled error.", object.getUniqueId(), t);
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
            return MyResultCode.SUCCESS;
        }
        else {
            error("is already in ", object, sceneGrid);
            return MyResultCode.SERVER_ERROR;
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
            return MyResultCode.SUCCESS;
        }
        else {
            error("wasn't in", object, sceneGrid);
            return MyResultCode.SERVER_ERROR;
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
                return MyResultCode.SCENE_MOVE_FAR;
            }
            if (Math.abs(oldSceneGrid.getGridY() - newSceneGrid.getGridY()) > 1){
                return MyResultCode.SCENE_MOVE_FAR;
            }
            if (oldSceneGrid.remove(object)) {
                if (newSceneGrid.add(object)) {
                }
                else {
                    error("is already in ", object, newSceneGrid);
                    return MyResultCode.SERVER_ERROR;
                }
            }
            else {
                error("wasn't in", object, oldSceneGrid);
                return MyResultCode.SERVER_ERROR;
            }
        }
        object.setPos(this, posXYZ);
        notify.onMoveTo(object, oldSceneGrid, newSceneGrid);
        return MyResultCode.SUCCESS;
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
