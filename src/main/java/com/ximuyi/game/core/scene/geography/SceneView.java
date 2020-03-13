package com.ximuyi.game.core.scene.geography;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.ximuyi.core.utils.RandomUtil;

public class SceneView {

    private final ScenePix[][] pixes;

    public SceneView(ScenePix[][] pixes) {
       this.pixes = new ScenePix[pixes.length][];
        for (int i = 0; i < pixes.length; i++) {
            this.pixes[i] = new ScenePix[pixes[i].length];
            for (int j = 0; j < pixes[i].length; j++) {
                this.pixes[i][j] = pixes[i][j].toClone();
            }
        }
    }

    public ScenePix[][] getPixes() {
        return pixes;
    }

    public SceneView drawPath(PixXYZ begin, PixXYZ end, List<PixXYZ> pixXYZList) {
        pixXYZList.forEach( pixXYZ -> {
            pixes[pixXYZ.x][pixXYZ.y].setValue(ScenePix.WALK);
        });
        pixes[begin.x][begin.y].setValue(ScenePix.FLAT);
        pixes[end.x][end.y].setValue(ScenePix.FLAT);
        return this;
    }

    public SceneView drawObject(PixXYZ pixXYZ) {
        pixes[pixXYZ.x][pixXYZ.y].setValue(ScenePix.OBJECT);
        return this;
    }

    public String[] getMap(){
        return build(pixes);
    }

    public static final ScenePix[][] build(String[] map){
        int count = 10;
        int totalCount = count * count;
        Queue<String[]> copyList = new ArrayDeque<>();
        for (int i = 0; i < totalCount; i++){
            String[] copy = new String[map.length];
            int k = RandomUtil.nextInt(map.length);
            for (int j = 0; j < map.length; j++) {
                copy[j] = map[k++ % map.length];
            }
            copyList.add(copy);
        }
        List<String> mapList = new ArrayList<>();
        for (int i = 0; i < map.length * count; i++){
            mapList.add("");
        }
        for (int j = 0; j < count; j++){
            List<String> mapList0 = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String[] copy = copyList.poll();
                for (String s : copy) {
                    mapList0.add(s);
                }
            }
            for (int k = 0; k < mapList0.size(); k++) {
                mapList.set(k, mapList0.get(k) + mapList.get(k));
            }
        }
        map = mapList.toArray(new String[0]);
        ScenePix[][] pixes = new ScenePix[map.length][];
        for (int i = 0; i < map.length; i++) {
            pixes[i] = new ScenePix[map[i].length()];
            for (int j = 0; j < map[i].length(); j++){
                pixes[i][j] = new ScenePix(i, j, map[i].charAt(j));
            }
        }
        return pixes;
    }

    private static final String[] build(ScenePix[][] pixes){
        String[] map = new String[pixes.length];
        for (int i = 0; i < map.length; i++) {
            map[i] = "";
            for (int j = 0; j < pixes[i].length; j++){
                map[i] += pixes[i][j].toChar();
            }
        }
        return map;
    }
}
