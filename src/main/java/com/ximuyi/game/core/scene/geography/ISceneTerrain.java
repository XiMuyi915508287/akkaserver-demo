package com.ximuyi.game.core.scene.geography;

import com.ximuyi.game.core.scene.ISceneGrid;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface ISceneTerrain extends IPixConvert {

    int getXUnit();

    int getYUnit();
    /**
     *
     * @param pixXYZ
     * @return
     */
    boolean canWalk(PixXYZ pixXYZ);

    /**
     * 是否有陷阱
     * @param pixXYZ
     * @return
     */
    boolean trapCovered(PixXYZ pixXYZ);

    /**
     * 增加陷阱
     * @param trap
     */
    void addTrap(SceneTrap trap);

    /**
     * 移除陷阱
     * @param uniqueId
     * @return
     */
    SceneTrap removeTrap(int uniqueId);

    /***
     * 摧毁障碍
     * @param rectangle
     * @param value >=当前障碍即可摧毁
     * @return
     */
    boolean destroy(PixRectangle rectangle, int value);

    /***
     * 放置障碍物
     * @param rectangle
     * @param value
     * @return
     */
    boolean obstacle(PixRectangle rectangle, int value);

    ISceneGrid get(PixXYZ sceneXYZ);

    ISceneGrid get(int gridX, int gridY);

    void forGrid(Consumer<ISceneGrid> consumer, Predicate<ISceneGrid> predicate);

    PixXYZ availablePix(PixXYZ pixXYZ);

    SceneView getView();
}
