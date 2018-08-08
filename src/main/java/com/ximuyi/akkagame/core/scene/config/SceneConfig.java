package com.ximuyi.akkagame.core.scene.config;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class SceneConfig {

    private static final SceneConfig instance = new SceneConfig();

    public static SceneConfig getInstance() {
        return instance;
    }

    private final Map<Integer, SceneDefine> scenes;

    private SceneConfig() {
        ImmutableMap.Builder<Integer, SceneDefine> builder = ImmutableMap.builder();
        builder.put(1, new SceneDefine(1));
        builder.put(2, new SceneDefine(2));
        this.scenes = builder.build();
    }

    public SceneDefine getDefine(int defineId){
        return scenes.get(defineId);
    }
}
