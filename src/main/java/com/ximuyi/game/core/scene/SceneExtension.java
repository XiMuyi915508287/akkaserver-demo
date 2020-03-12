package com.ximuyi.game.core.scene;

import com.ximuyi.game.common.extension.Extension;
import com.ximuyi.game.core.sceneobject.ai.AIObjectManager;

public class SceneExtension extends Extension {

    public static final short ID = (short)2;

    public SceneExtension(short extension) {
        super(extension);
    }

    @Override
    protected void onInit() {
        super.onInit();
        AIObjectManager.getInstance().init();
        SceneManager.getInstance().init();
//        SceneService.getInstance().init();
    }

}
