package com.ximuyi.akkagame.core.scene.notify;

import com.google.protobuf.MessageLite;
import com.ximuyi.akkagame.common.extension.RequestExtension;
import com.ximuyi.akkagame.common.util.DoubleEntry;
import com.ximuyi.akkagame.core.scene.*;
import com.ximuyi.akkagame.core.sceneobject.ISceneObject;
import com.ximuyi.akkagame.core.sceneobject.ObjectType;
import com.ximuyi.akkagame.core.sceneobject.ScenePlayer;
import com.ximuyi.akkagame.server.coder.ICodeSerialize;
import com.ximuyi.akkagame.server.proto.ProtoScene;
import com.ximuyi.akkaserver.extension.Command;
import com.ximuyi.akkaserver.extension.ICommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Supplier;

public class SceneNotify implements ISceneNotify {
    private static final Logger logger = LoggerFactory.getLogger(SceneNotify.class);

    private final IScene scene;
    private final Map<Long, SceneGridNotify> cache;

    public SceneNotify(IScene scene) {
        this.scene = scene;
        this.cache = new HashMap<>();
        scene.forGrid( grid-> cache.put(grid.getUniqueId(), new SceneGridNotify(grid)), null);
        SceneNotifyScheduler.getInstance().schedule(scene.getUniqueId(), this, 1000L, 300L);
    }

    @Override
    public void onJoin(ISceneObject object, ISceneGrid toGrid) {
        onCache(aroundChange(toGrid), () -> new JoinCahce(object));
    }

    @Override
    public void onMoveTo(ISceneObject object, ISceneGrid leaveGrid, ISceneGrid toGrid) {
        onCache(moveGetChange(leaveGrid, toGrid, true), ()-> new JoinCahce(object));
        onCache(moveGetChange(leaveGrid, toGrid, false), ()-> new LeaveCahce(object));
        onCache(moveNoChange(leaveGrid, toGrid), () -> new MoveCahce(object));
    }

    @Override
    public void onLeave(ISceneObject object, ISceneGrid toGrid) {
        onCache(aroundChange(toGrid), () -> new LeaveCahce(object));
    }

    @Override
    public void onTick() {
        for (SceneGridNotify notify : cache.values()) {
            try {
                notify.onTick();
            } catch (Throwable t) {
                logger.error(notify.toString(), t);
            }
        }
    }

    @Override
    public void dispose() {
        SceneNotifyScheduler.getInstance().cancel(scene.getUniqueId());
    }

    private <T extends NotifyCahce< ? extends MessageLite>> void onCache(List<ISceneGrid> gridList, Supplier<T> supplier) {
        for (ISceneGrid grid : gridList) {
            SceneGridNotify notify = cache.get(grid.getUniqueId());
            notify.add(supplier.get());
        }
    }

    /**
     */
    private List<ISceneGrid> aroundChange(ISceneGrid grid){
        List<ISceneGrid> gridList = new ArrayList<>(9 );
        gridList.add(grid);
        PathFinder.foreachDirection( (x, y)->{
            int xGrid = grid.getGridX() + x;
            int yGrid = grid.getGridY() + y;
            ISceneGrid sceneGrid = scene.getTerrain().get(xGrid, yGrid);
            if (sceneGrid != null){
                gridList.add(sceneGrid);
            }
        });
        return gridList;
    }

    /**
     * @param leaveGrid
     * @param toGrid
     * @param isJoin 是加入或者离开 true 加入
     * @return
     */
    private List<ISceneGrid> moveGetChange(ISceneGrid leaveGrid, ISceneGrid toGrid, boolean isJoin){
        // -1 <= xOffset <= 1  && -1 <= yOffset <= 1
        int xOffset = 0, yOffset = 0;
        xOffset = isJoin ? (toGrid.getGridX() - leaveGrid.getGridX()) : (leaveGrid.getGridX() - toGrid.getGridX());
        yOffset = isJoin ? (toGrid.getGridY() - leaveGrid.getGridY()) : (leaveGrid.getGridY() - toGrid.getGridY());

        if (xOffset == 0 && yOffset == 0){
            return Collections.emptyList();
        }
        DoubleEntry<Integer, Integer> gridXY;
        if (isJoin){
            gridXY = new DoubleEntry(toGrid.getGridX() + xOffset, toGrid.getGridY() + yOffset);
        }else {
            gridXY = new DoubleEntry(leaveGrid.getGridX() + xOffset, leaveGrid.getGridY() + yOffset);
        }
        List<DoubleEntry<Integer, Integer>> gridXYList = new ArrayList<>(5);
        gridXYList.add(gridXY);
        if (xOffset != 0 && yOffset != 0){
            gridXYList.add(new DoubleEntry(gridXY.first, gridXY.second - yOffset));
            gridXYList.add(new DoubleEntry(gridXY.first, gridXY.second - 2 * yOffset));
            gridXYList.add(new DoubleEntry(gridXY.first - xOffset, gridXY.second));
            gridXYList.add(new DoubleEntry(gridXY.first - 2* xOffset, gridXY.second));
        }
        else if (xOffset != 0 && yOffset == 0){
            gridXYList.add(new DoubleEntry(gridXY.first, gridXY.second - 1));
            gridXYList.add(new DoubleEntry(gridXY.first, gridXY.second + 1));
        }
        else if (xOffset == 0 && yOffset != 0){
            gridXYList.add(new DoubleEntry(gridXY.first - 1, gridXY.second));
            gridXYList.add(new DoubleEntry(gridXY.first + 1, gridXY.second));
        }
        List<ISceneGrid> gridList = new ArrayList<>(gridXYList.size());
        for (DoubleEntry<Integer, Integer> entry : gridXYList) {
            ISceneGrid sceneGrid = scene.getTerrain().get(entry.first, entry.second);
            if (sceneGrid != null){
                gridList.add(sceneGrid);
            }
        }
        return gridList;
    }

    private List<ISceneGrid> moveNoChange(ISceneGrid leaveGrid, ISceneGrid toGrid){
        // -1 <= xOffset <= 1  && -1 <= yOffset <= 1
        int xOffset = 0, yOffset = 0;
        xOffset = toGrid.getGridX() - leaveGrid.getGridX();
        yOffset = toGrid.getGridY() - leaveGrid.getGridY();
        if (xOffset == 0 && yOffset == 0){
            return aroundChange(toGrid);
        }
        List<DoubleEntry<Integer, Integer>> gridXYList = new ArrayList<>(6);
        gridXYList.add(new DoubleEntry(leaveGrid.getGridX(), leaveGrid.getGridY()));
        gridXYList.add(new DoubleEntry(leaveGrid.getGridX() + xOffset, leaveGrid.getGridY() + yOffset));
        if (xOffset != 0 && yOffset != 0){
            gridXYList.add(new DoubleEntry(leaveGrid.getGridX() + xOffset, leaveGrid.getGridY()));
            gridXYList.add(new DoubleEntry(leaveGrid.getGridX(), leaveGrid.getGridY() + yOffset));
        }
        else if (xOffset != 0 && yOffset == 0){
            gridXYList.add(new DoubleEntry(leaveGrid.getGridX(), leaveGrid.getGridY() - 1));
            gridXYList.add(new DoubleEntry(leaveGrid.getGridX(), leaveGrid.getGridY() +1));
            gridXYList.add(new DoubleEntry(toGrid.getGridX(), toGrid.getGridY() - 1));
            gridXYList.add(new DoubleEntry(toGrid.getGridX(), toGrid.getGridY() +1));
        }
        else if (xOffset == 0 && yOffset != 0){
            gridXYList.add(new DoubleEntry(leaveGrid.getGridX() - 1, leaveGrid.getGridY()));
            gridXYList.add(new DoubleEntry(leaveGrid.getGridX() + 1, leaveGrid.getGridY()));
            gridXYList.add(new DoubleEntry(toGrid.getGridX() - 1, toGrid.getGridY()));
            gridXYList.add(new DoubleEntry(toGrid.getGridX() + 1, toGrid.getGridY()));
        }
        List<ISceneGrid> gridList = new ArrayList<>(gridXYList.size());
        for (DoubleEntry<Integer, Integer> entry : gridXYList) {
            ISceneGrid sceneGrid = scene.getTerrain().get(entry.first, entry.second);
            if (sceneGrid != null){
                gridList.add(sceneGrid);
            }
        }
        return gridList;
    }


    private void testMove(){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 7; j++) {
                ISceneGrid toGrid = null;
                while (toGrid == null){
                    DoubleEntry<Integer, Integer> entry = PathFinder.randomDirection();
                    toGrid = scene.getTerrain().get(i + entry.first, j + entry.second);
                }
                ISceneGrid leaveGrid =  scene.getTerrain().get(i, j);
                printMove(leaveGrid, toGrid, moveGetChange(leaveGrid, toGrid, true), "加入的新区域");
                printMove(leaveGrid, toGrid, moveGetChange(leaveGrid, toGrid, false), "离开的旧区域");
                printMove(leaveGrid, toGrid, moveNoChange(leaveGrid, toGrid), "移动的区域");
            }
        }
    }

    private void printMove(ISceneGrid leaveGrid, ISceneGrid toGrid,  List<ISceneGrid> gridList, String describe){
        int xlen = Math.max(8, Math.max(leaveGrid.getGridX(), toGrid.getGridX()) + 3);
        int ylen = Math.max(8, Math.max(leaveGrid.getGridY(), toGrid.getGridY()) + 3);
        int[][] map = new int[xlen][ylen];
        String string = String.format("leaveGrid=%s toGrid=%s\ngridList=%s\n", leaveGrid, toGrid, gridList);
        StringBuffer buffer = new StringBuffer(string);
        for (int i = 0; i < xlen; i++) {
            for (int j = 0; j < ylen; j++) {
                if (i == leaveGrid.getGridX() && j == leaveGrid.getGridY()){
                    buffer.append("A");
                }
                else if (i == toGrid.getGridX() && j == toGrid.getGridY()){
                    buffer.append("B");
                }
                else {
                    buffer.append(".");
                }
            }
            buffer.append("\n");
        }
        for (ISceneGrid grid : gridList) {
            map[grid.getGridX()][grid.getGridY()] = 1;
        }
        buffer.append("------------------------" + describe + "--------------------\n");
        for (int i = 0; i < xlen; i++) {
            for (int j = 0; j < ylen; j++) {
                if (map[i][j] == 1){
                    buffer.append("*");
                }
                else {
                    buffer.append(".");
                }
            }
            buffer.append("\n");
        }
        buffer.append("==========================================================\n");
        System.out.println(buffer);
    }

    private static class SceneGridNotify implements ICodeSerialize<ProtoScene.SceneTickResponse>, ISceneSchedule {

        private static final ICommand Command = new Command(SceneExtension.ID, (short) 1);

        private final ISceneGrid grid;
        private final ConcurrentLinkedDeque<NotifyCahce< ? extends MessageLite>> deque;

        public SceneGridNotify(ISceneGrid grid) {
            this.grid = grid;
            this.deque = new ConcurrentLinkedDeque<>();
        }

        public void add(NotifyCahce< ? extends MessageLite> message){
            deque.add(message);
        }

        @Override
        public ProtoScene.SceneTickResponse serialize() {
            ProtoScene.SceneTickResponse.Builder builder = ProtoScene.SceneTickResponse.newBuilder();
            int index = 0;
            while (!deque.isEmpty()){
                NotifyCahce< ? extends MessageLite> message = deque.poll();
                if (message instanceof JoinCahce){
                    builder.addJoinScene(((JoinCahce)message).serialize(index));
                }else if (message instanceof MoveCahce){
                    builder.addMoveScene(((MoveCahce)message).serialize(index));
                }else if (message instanceof LeaveCahce){
                    builder.addLeaveScene(((LeaveCahce)message).serialize(index));
                }
                else {
                    throw new UnsupportedOperationException(message.toString());
                }
                index++;
            }
            return index > 0 ? builder.build() : null;
        }

        @Override
        public void onTick() {
            ProtoScene.SceneTickResponse response = serialize();
            if (response == null){
                return;
            }
            Collection<ScenePlayer> objects = grid.objects(ObjectType.Player);
            RequestExtension.respond(objects, Command, response);
        }

        @Override
        public String toString() {
            return String.format("grid[%d,%d] length:%d", grid.getGridX(), grid.getGridY(), deque.size());
        }
    }
}
