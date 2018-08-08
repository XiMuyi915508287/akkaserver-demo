package com.ximuyi.akkagame.core.scene;

import com.ximuyi.akkagame.common.util.DoubleEntry;
import com.ximuyi.akkagame.common.util.ResultCode;
import com.ximuyi.akkagame.common.util.TaskScheduler;
import com.ximuyi.akkagame.core.scene.config.SceneConfig;
import com.ximuyi.akkagame.core.scene.config.SceneDefine;
import com.ximuyi.akkagame.core.scene.geography.SceneTerrain;
import com.ximuyi.akkaserver.core.CoreAccessor;

import java.util.concurrent.atomic.AtomicLong;

public class SceneManager {
    private static final String[] MAP = new String[]{
            "......AAAAAAA...........................",
            "............A...........................",
            "............A..........A................",
            "............A..........A................",
            "............A..........A................",
            "............A..........A................",
            ".......................A................",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA..AAAAAAA",
            ".......................A................",
            ".......................A................",
            "..............AAAAAAAAAA................",
            "........................................",
            "........................................",
            "...................AAAAAAAAAAAAA........",
            "........................................",
            "........................................",
    };
    private static final SceneManager instance = new SceneManager();

    public static SceneManager getInstance() {
        return instance;
    }

    private final AtomicLong uniqueIdGen = new AtomicLong(0);

    private final TaskScheduler<IScene> sceneScheduler;

    public SceneManager() {
        this.sceneScheduler = new TaskScheduler<>(CoreAccessor.getLocator().getScheduler());
    }

    public void init(){
    }

    public IScene getScene(long uniqueId){
        return sceneScheduler.get(uniqueId);
    }

    public DoubleEntry<ResultCode, IScene> create(int defineId, SceneOwn sceneOwn){
        SceneDefine define = SceneConfig.getInstance().getDefine(defineId);
        if (define == null){
            return new DoubleEntry<>(ResultCode.PARAM_ERROR, null);
        }
        IScene scene = new Scene(uniqueIdGen.incrementAndGet(), defineId, sceneOwn,
                                 new SceneTerrain(500, 500, MAP));
        sceneScheduler.schedule(scene.getUniqueId(), scene, 2000L, 1000L);
        return new DoubleEntry<>(ResultCode.SUCCESS, scene);
    }

    public void remove(int uniqueId){
        IScene scene = sceneScheduler.cancel(uniqueId);
        if (scene != null){
            ((ISceneDispose)scene).dispose();
        }
    }
}
