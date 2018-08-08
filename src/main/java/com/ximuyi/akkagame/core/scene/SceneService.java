package com.ximuyi.akkagame.core.scene;

import com.ximuyi.akkagame.common.User;
import com.ximuyi.akkagame.common.util.DoubleEntry;
import com.ximuyi.akkagame.common.util.ResultCode;
import com.ximuyi.akkagame.core.scene.geography.PixXYZ;
import com.ximuyi.akkagame.core.sceneobject.ISceneObject;
import com.ximuyi.akkagame.core.sceneobject.ScenePlayer;
import com.ximuyi.akkagame.core.sceneobject.component.ObjetCompAI;

public class SceneService {
    private static final SceneService instance = new SceneService();

    public static SceneService getInstance() {
        return instance;
    }

    public SceneService() {
    }

    public void init(){
        for (int i = 0; i < 10; i++) {
            DoubleEntry<ResultCode, IScene> entry = SceneManager.getInstance().create(1, SceneOwn.System);
            int number = (i + 1) * 100;
            for (int j = 0; j < number; j++) {
                User user = new User(i * 100 + j + 1, String.format("%d-%d", i, j));
                ISceneObject object = new ScenePlayer(user, user.getUserId());
                object.addComponent(new ObjetCompAI(1));
                PixXYZ pixXYZ = entry.second.getTerrain().availablePix(null);
                entry.second.join(object, entry.second.getTerrain().transform(pixXYZ));
            }
        }
    }

    public ResultCode join(User user, int uniqueId, float x, float y){
        IScene scene = SceneManager.getInstance().getScene(uniqueId);
        if (scene == null){
            return ResultCode.PARAM_ERROR;
        }
        throw new UnsupportedOperationException();
    }
}
