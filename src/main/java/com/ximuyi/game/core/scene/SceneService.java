package com.ximuyi.game.core.scene;

import com.ximuyi.core.coder.ResultCode;
import com.ximuyi.game.common.MyUser;
import com.ximuyi.game.common.util.Args;
import com.ximuyi.game.common.util.MyResultCode;
import com.ximuyi.game.core.scene.geography.PixXYZ;
import com.ximuyi.game.core.scene.object.ISceneObject;
import com.ximuyi.game.core.scene.object.ScenePlayer;
import com.ximuyi.game.core.scene.object.component.ObjectCompAI;

public class SceneService {
    private static final SceneService instance = new SceneService();

    public static SceneService getInstance() {
        return instance;
    }

    public SceneService() {
    }

    public void init(){
        Args.Two<ResultCode, IScene> entry = SceneManager.getInstance().create(1, SceneOwn.System);
        int number = 10;
        for (int j = 0; j < number; j++) {
            MyUser user = new MyUser(j, String.format("Jim%s", j));
            ISceneObject object = new ScenePlayer(user, user.getUserId());
            object.addComponent(new ObjectCompAI(2000 + j));
            PixXYZ pixXYZ = entry.arg1.getTerrain().availablePix(null);
            entry.arg1.join(object, entry.arg1.getTerrain().transform(pixXYZ));
        }
    }

    public ResultCode join(MyUser user, int uniqueId, float x, float y){
        IScene scene = SceneManager.getInstance().getScene(uniqueId);
        if (scene == null){
            return MyResultCode.PARAM_ERROR;
        }
        throw new UnsupportedOperationException();
    }
}
