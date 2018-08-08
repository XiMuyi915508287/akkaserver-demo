package com.ximuyi.akkagame.core.scene;

import com.ximuyi.akkagame.common.extension.RequestExtension;
import com.ximuyi.akkagame.core.sceneobject.ai.AIObjectManager;

public class SceneExtension extends RequestExtension {

    public static final short ID = (short)2;

    public SceneExtension(short extension) {
        super(extension);
    }

    @Override
    protected void onInit() {
        super.onInit();
        AIObjectManager.getInstance().init();
        SceneManager.getInstance().init();
        SceneService.getInstance().init();
    }

}
