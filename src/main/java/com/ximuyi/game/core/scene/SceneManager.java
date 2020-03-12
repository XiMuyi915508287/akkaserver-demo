package com.ximuyi.game.core.scene;

import java.util.concurrent.atomic.AtomicLong;

import com.ximuyi.core.coder.ResultCode;
import com.ximuyi.core.core.CoreAccessor;
import com.ximuyi.game.common.util.Args;
import com.ximuyi.game.common.util.MyResultCode;
import com.ximuyi.game.common.util.MyScheduler;
import com.ximuyi.game.core.scene.config.SceneConfig;
import com.ximuyi.game.core.scene.config.SceneDefine;
import com.ximuyi.game.core.scene.geography.SceneTerrain;

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

    private final MyScheduler<IScene> sceneScheduler;

    public SceneManager() {
        this.sceneScheduler = new MyScheduler<>(CoreAccessor.getInstance().getScheduler());
    }

    public void init(){
    }

    public IScene getScene(long uniqueId){
        return sceneScheduler.get(uniqueId);
    }

    public Args.Two<ResultCode, IScene> create(int defineId, SceneOwn sceneOwn){
        SceneDefine define = SceneConfig.getInstance().getDefine(defineId);
        if (define == null){
            return Args.create(MyResultCode.PARAM_ERROR, null);
        }
        IScene scene = new Scene(uniqueIdGen.incrementAndGet(), defineId, sceneOwn,
                                 new SceneTerrain(500, 500, MAP));
        sceneScheduler.schedule(scene.getUniqueId(), scene, 2000L, 1000L);
        return Args.create(MyResultCode.SUCCESS, scene);
    }

    public void remove(int uniqueId){
        IScene scene = sceneScheduler.cancel(uniqueId);
        if (scene != null){
            ((ISceneDispose)scene).dispose();
        }
    }
}
