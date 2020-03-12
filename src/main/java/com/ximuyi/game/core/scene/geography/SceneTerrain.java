package com.ximuyi.game.core.scene.geography;

import com.ximuyi.game.common.util.RandomUtil;
import com.ximuyi.game.core.scene.ISceneGrid;
import com.ximuyi.game.core.scene.PathFinder;
import com.ximuyi.game.core.scene.PosXYZ;
import com.ximuyi.game.core.scene.SceneGrid;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SceneTerrain implements ISceneTerrain {
    private final int xUnit;
    private final int yUnit;
    private final ScenePix[][] pixes;
    private final ISceneGrid[][] grids;
    private Map<Long, List<ScenePix>> pathPixes;
    //暂时使用一个Map来处理，有什么高效率的方法嘛？
    private Map<Long, SceneTrap> traps;

    public SceneTerrain(int xUnit, int yUnit, String[] map) {
        this.xUnit = xUnit;
        this.yUnit = yUnit;
        this.pixes = SceneView.build(map);
        int x = (pixes.length - 1) / xUnit + 1;
        int y = (Arrays.asList(pixes).stream().mapToInt( value-> value.length).max().getAsInt() - 1 ) / yUnit + 1;
        this.grids = new ISceneGrid[x][y];
        for (int i = 0; i < grids.length; i++) {
            for (int j = 0; j < grids[i].length; j++) {
                grids[i][j] = new SceneGrid(i, j);
            }
        }
        this.traps = new ConcurrentHashMap<>();
        this.pathPixes = buildPathScenePix();
        this.randomTrap();
    }

    private void randomTrap(){
        long uniqueId = 1;
        int xLength = pixes.length / 5;
        int yLength = pixes.length / 5;
        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < yLength; j++) {
                PixXYZ pixXYZ0 = new PixXYZ(i * xLength + 1, j * yLength + 1);
                PixXYZ pixXYZ1 = new PixXYZ(pixXYZ0.x + RandomUtil.nextInt(xLength - 1), pixXYZ0.y + RandomUtil.nextInt(yLength - 1));
                addTrap(new SceneTrap(pixXYZ0, pixXYZ1, uniqueId++, 1));
            }
        }
    }

    @Override
    public int getXUnit() {
        return xUnit;
    }

    @Override
    public int getYUnit() {
        return yUnit;
    }

    @Override
    public boolean canWalk(PixXYZ pixXYZ) {
        ScenePix pix = get(pixXYZ.x, pixXYZ.y, pixes);
        return pix == null ? false : pix.canWalk();
    }

    @Override
    public boolean trapCovered(PixXYZ pixXYZ) {
        for (SceneTrap trap : traps.values()) {
            if (trap.isCovered(pixXYZ)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void addTrap(SceneTrap trap) {
        traps.put(trap.getUniqueId(), trap);
    }

    @Override
    public SceneTrap removeTrap(int uniqueId) {
        return traps.remove(uniqueId);
    }

    @Override
    public boolean destroy(PixRectangle rectangle, int value) {
        boolean[] success = new boolean[]{false};
        rectangle.forXY( (x, y)->{
            ScenePix pix = get(x, y, pixes);
            if (pix == null || !pix.isValid(value)){
                return;
            }
            pix.setValue(0);
            success[0] = true;
        });
        this.pathPixes = buildPathScenePix();
        return success[0];
    }

    @Override
    public boolean obstacle(PixRectangle rectangle, int value) {
        if (value < 0){
            throw new UnsupportedOperationException(String.valueOf(value));
        }
        boolean[] success = new boolean[]{false};
        rectangle.forXY( (x, y)->{
            ScenePix pix = get(x, y, pixes);
            if (pix == null || !pix.isValid(value)){
                return;
            }
            pix.setValue(value);
            success[0] = true;
        });
        this.pathPixes = buildPathScenePix();
        return success[0];
    }

    @Override
    public ISceneGrid get(PixXYZ pixXYZ) {
        int i = pixXYZ.x / xUnit;
        int j = pixXYZ.y / yUnit;
        return get(i, j, grids);
    }

    @Override
    public ISceneGrid get(int gridX, int gridY) {
        return get(gridX, gridY, grids);
    }

    @Override
    public void forGrid(Consumer<ISceneGrid> consumer, Predicate<ISceneGrid> predicate) {
        for (int i = 0; i < grids.length; i++) {
            for (int j = 0; j < grids[i].length; j++) {
                if (predicate == null || predicate.test(grids[i][j])){
                    consumer.accept(grids[i][j]);
                }
            }
        }
    }

    @Override
    public PixXYZ availablePix(PixXYZ pixXYZ) {
        Map<Long, List<ScenePix>> map = this.pathPixes;
        List<ScenePix> scenePixList = null;
        if (pixXYZ == null){
            List<Long> keys = new ArrayList<>(map.keySet());
            int index = RandomUtil.nextInt(keys.size());
            scenePixList = map.get(keys.get(index));
        }
        else {
            while (scenePixList == null){
                ISceneGrid grid = get(pixXYZ);
                scenePixList = PathFinder.foreachDirection((x, y)->{
                    ISceneGrid randomGrid = get(grid.getGridX() + x, grid.getGridY() + y);
                    return randomGrid == null ? null : map.get(randomGrid.getUniqueId());
                });
            }
        }
        int index = RandomUtil.nextInt(scenePixList.size());
        return scenePixList.get(index).toPixXYZ();
    }

    @Override
    public SceneView getView() {
        SceneView view = new SceneView(pixes);
        for (SceneTrap trap : traps.values()) {
            trap.forXY( (x, y)->{
                ScenePix pix = get(x, y, view.getPixes());
                if (pix == null || !pix.canWalk()){
                    return;
                }
                pix.setValue(ScenePix.TRAP);
            });
        }
        return view;
    }

    @Override
    public PosXYZ transform(PixXYZ sceneXYZ) {
        return new PosXYZ(sceneXYZ.x, sceneXYZ.y, sceneXYZ.z);
    }

    @Override
    public PixXYZ transform(PosXYZ posXYZ) {
        return new PixXYZ((int)posXYZ.x, (int)posXYZ.y, (int)posXYZ.z);
    }

    private Map<Long, List<ScenePix>> buildPathScenePix() {
        ScenePix[][] pixes = this.pixes;
        Map<Long, List<ScenePix>> map = new HashMap<>();
        for (int i = 0; i < pixes.length; i++) {
            for (int j = 0; j < pixes[i].length; j++) {
                if (pixes[i][j].canWalk()) {
                    long key = get(pixes[i][j].toPixXYZ()).getUniqueId();
                    List<ScenePix> pixList = map.get(key);
                    if (pixList == null){
                        pixList = new ArrayList<>();
                        map.put(key, pixList);
                    }
                    pixList.add(pixes[i][j]);
                }
            }
        }
        return map;
    }

    private static  <T> T get(int x, int y, T[][] datas){
        if (x < 0 || x >= datas.length){
            return null;
        }
        if (y < 0 || y >= datas[x].length){
            return null;
        }
        return datas[x][y];
    }
}
