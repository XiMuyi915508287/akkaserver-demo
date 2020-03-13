package com.ximuyi.game.core.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.ximuyi.game.common.util.Args;
import com.ximuyi.core.utils.RandomUtil;
import com.ximuyi.game.core.scene.geography.ISceneTerrain;
import com.ximuyi.game.core.scene.geography.PixXYZ;
import com.ximuyi.game.core.scene.object.ISceneObject;

public class PathFinder {
    //下面两个数组，相同下标的值中，(DIR_X，DIR_Y)的值构成一个方向
    private static final int[] DIR_X = new int[] { 1,  0, -1,  0,  1, -1, -1,  1};
    private static final int[] DIR_Y = new int[] { 0,  1,  0, -1,  1,  1, -1, -1};

    private static final int DIRECTION_NUM = 8;//方向的数目

    private static final int DIR_V_STRAIGHT = 10; //直走的耗散值为10
    private static final int DIR_V_DIAGONAL = 14; //斜角的耗散值为14

    private final IScene scene;
    private final ISceneTerrain terrain;

    public PathFinder(IScene scene) {
        this.scene = scene;
        this.terrain = scene.getTerrain();
    }

    public List<PixXYZ> searchPath(ISceneObject object, PosXYZ beginPos, PosXYZ endPos) {
        return searchPath(object, beginPos, endPos, (pathPix -> pathPix.pixXYZ));
    }

    public List<PixXYZ> searchPath(ISceneObject object, PixXYZ beginPix, PixXYZ endPix) {
        return searchPath(object, terrain.transform(beginPix), terrain.transform(endPix), (pathPix -> pathPix.pixXYZ));
    }

    private <T> List<T> searchPath(ISceneObject object, PosXYZ beginPos, PosXYZ endPos, Function<PathPix, T> function) {
        Map<Long, PathFindPos> openMap = new HashMap<>(); //OpenList，为了方便移除操作，使用map
        Map<Long, PathFindPos> closeMap = new HashMap<>(); //CloseList，为了方便操作，使用map

        PathPix endPix = new PathPix(endPos);
        PathFindPos pathFindPos = new PathFindPos(new PathPix(beginPos), null, endPix);
        openMap.put(pathFindPos.curPix.key, pathFindPos);

        while(openMap.size() > 0 && !openMap.containsKey(endPix.key)) {
            PathFindPos curPos = null;
            for(PathFindPos pathPosition : openMap.values()) { //寻找F值最小的一个点
                if(curPos == null || pathPosition.F < curPos.F) {
                    curPos = pathPosition;
                }
            }
            openMap.remove(curPos.curPix.key);
            closeMap.put(curPos.curPix.key, curPos);


            for(int i=0; i<DIRECTION_NUM; i++) {
                int xGrid = curPos.curPix.getxGrid() + DIR_X[i];
                int zGrid = curPos.curPix.getyGrid() + DIR_Y[i];
                PathPix iPix = new PathPix(xGrid, zGrid);
                //如果节点不可到达或者已经被访问过（放到closeMap中）则跳过
                if(!terrain.canWalk(iPix.pixXYZ) || closeMap.containsKey(iPix.key)) {
                    continue;
                }
                int weight = object.trapWeight() > 0 && terrain.trapCovered(iPix.pixXYZ) ? object.trapWeight() : 0;
                PathFindPos pathPos = new PathFindPos(iPix, curPos, endPix, weight);
                PathFindPos oldPathPos = openMap.get(iPix.key);
                if (oldPathPos == null){
                    openMap.put(pathPos.curPix.key, pathPos);
                }
                else if (oldPathPos.F > pathPos.F){
                    openMap.put(iPix.key, pathPos);
                }
            }
        }
        if(openMap.size() <= 0 || !openMap.containsKey(endPix.key)) {
            return null;
        }

        //构造一条反向的路径，并去除方向相同的点
        ArrayList<PathFindPos> reverse = new ArrayList<>();
        PathFindPos pathPosition = openMap.get(endPix.key);
        pathPosition.calculateDirection();
        int direction = pathPosition.direction;
        while(pathPosition.prePathPos != null) {
//            if(pathPosition.direction != direction) { //去除方向相同的点
                reverse.add(pathPosition);
//            }
            pathPosition = pathPosition.prePathPos;
            pathPosition.calculateDirection();
        }
        reverse.add(pathPosition);

        //构造出最终的路径
        ArrayList<T> finalPath = new ArrayList<T>();
        for(int i=reverse.size() - 1; i >= 0; i--) {
            PathFindPos findPos = reverse.get(i);
            finalPath.add(function.apply(findPos.curPix));
        }
        return finalPath;
    }

    public final class PathPix {
        public PosXYZ posXYZ;
        public PixXYZ pixXYZ;

        private long key;

        private PathPix(int xGrid, int yGrid) {
            this.pixXYZ = new PixXYZ(xGrid, yGrid);
            this.posXYZ = terrain.transform(pixXYZ);
            this.key = (((long)pixXYZ.x) << 32) | pixXYZ.y;
        }

        private PathPix(PosXYZ posXYZ) {
            this.posXYZ = posXYZ;
            this.pixXYZ = terrain.transform(posXYZ);
            this.key = (((long)pixXYZ.x) << 32) | pixXYZ.y;
        }

        public int getxGrid() {
            return pixXYZ.x;
        }

        public int getyGrid() {
            return pixXYZ.y;
        }
    }

    private final class PathFindPos{
        PathPix curPix;

        PathFindPos prePathPos;

        int F; //F = G + H,表示对象从起点经过当前节点p到达目标节点预计总消耗值
        int G; //G值表示对象从起点到达当前节点时需要消耗值
        int H; //H值则表示对象从当前节点预计到达目标节点需要消耗值

        int direction; //朝向，因为是指向上一点的朝向，所以跟实际方向应该是反的

        private PathFindPos(PathPix curPix, PathFindPos prePathPos, PathPix endPix, int weight) {
            this.curPix = curPix;

            this.prePathPos = prePathPos;

            int dir_v = this.prePathPos == null ? 0 : (curPix.getxGrid() == prePathPos.curPix.getxGrid()
                    || curPix.getyGrid() == prePathPos.curPix.getyGrid() ? DIR_V_STRAIGHT : DIR_V_DIAGONAL);
            this.G = prePathPos == null ? 0 : this.prePathPos.G + dir_v;
            this.H = (Math.abs(curPix.getxGrid() - endPix.getxGrid()) + Math.abs(curPix.getyGrid() - endPix.getyGrid())) * 10;
            this.F = G + H + weight * dir_v;

        }
        private PathFindPos(PathPix curPix, PathFindPos prePathPos, PathPix endPix) {
            this(curPix, prePathPos, endPix, 0);
        }


        private void calculateDirection() {
            if(prePathPos != null) {
                PathPix pre = prePathPos.curPix;
                PathPix cur = curPix;

                int d = -1;
                if(pre.getxGrid() > cur.getxGrid() && pre.getyGrid() < cur.getyGrid()) d = 1;
                else if(pre.getxGrid() == cur.getxGrid() && pre.getyGrid() < cur.getyGrid()) d = 2;
                else if(pre.getxGrid() < cur.getxGrid() && pre.getyGrid() < cur.getyGrid()) d = 3;
                else if(pre.getxGrid() > cur.getxGrid() && pre.getyGrid() == cur.getyGrid()) d = 4;
                else if(pre.getxGrid() < cur.getxGrid() && pre.getyGrid() == cur.getyGrid()) d = 6;
                else if(pre.getxGrid() > cur.getxGrid() && pre.getyGrid() > cur.getyGrid()) d = 7;
                else if(pre.getxGrid() == cur.getxGrid() && pre.getyGrid() > cur.getyGrid()) d = 8;
                else if(pre.getxGrid() < cur.getxGrid() && pre.getyGrid() > cur.getyGrid()) d = 9;
                direction = d;
            } else {
                direction = -1;
            }
        }
    }

    public static Args.Two<Integer, Integer> randomDirection(){
        int i = RandomUtil.nextInt(DIRECTION_NUM);
        return Args.create(DIR_X[i], DIR_Y[i]);
    }

    public static void foreachDirection(BiConsumer<Integer, Integer> consumer){
        for(int i=0; i< DIRECTION_NUM; i++) {
            consumer.accept(DIR_X[i], DIR_Y[i]);
        }
    }

    public static <T> T foreachDirection(BiFunction<Integer, Integer, T> function){
        int index = RandomUtil.nextInt(DIRECTION_NUM);
        int count = DIRECTION_NUM;
        while (count > 0){
            int i = (index++) % DIRECTION_NUM;
            T value = function.apply(DIR_X[i], DIR_Y[i]);
            if (value != null){
                return value;
            }
            count--;
        }
        return null;
    }
}
