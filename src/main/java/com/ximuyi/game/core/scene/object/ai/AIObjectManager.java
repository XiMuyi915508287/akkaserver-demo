package com.ximuyi.game.core.scene.object.ai;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.ximuyi.core.bt.BTManager;
import com.ximuyi.core.bt.BTNode;
import com.ximuyi.core.config.Configs;
import com.ximuyi.core.utils.RandomUtil;
import com.ximuyi.game.core.scene.object.ai.action.AIObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AIObjectManager extends BTManager<AIObjectContext> {

    private static final Logger logger = LoggerFactory.getLogger(AIObjectManager.class);

    private static final AIObjectManager instance = new AIObjectManager();

    public static AIObjectManager getInstance() {
        return instance;
    }

    private Map<Integer, BTNode<AIObjectContext>> id_root = Collections.emptyMap();

    private AIObjectManager() {
        loadConfigs();
    }

    @Override
    protected BTNode<AIObjectContext> createCustomNode(String type) {
        return AIObjectFactory.getAIAction(type);
    }

    private void loadConfigs() {
        createJson();
        String baseDirPath = Configs.getInstance().getFilePath("resources/json/object/ai");
        File baseDir = new File(baseDirPath);
        ImmutableMap.Builder<Integer, BTNode<AIObjectContext>>  builder = ImmutableMap.builder();
        for (File file : baseDir.listFiles(file -> (file.getName().endsWith(".json")))) {
            try {
                String fileFullName = file.getName();
                String fileName = fileFullName.substring(0, fileFullName.lastIndexOf("."));
                int id = Integer.parseInt(fileName);

                String content = Files.asCharSource(file, Charset.forName("UTF-8")).read();
                JSONObject json = JSONObject.parseObject(content);
                BTNode<AIObjectContext> root = createNode(json.getJSONObject("root"));
                builder.put(id, root);

            } catch (Throwable t) {
                logger.error("Init AI Error, file: " + file.getName(), t);
            }
        }

        this.id_root = builder.build();
    }

    public void init() {
    }

    public BTNode<AIObjectContext> getAI(int id) {
        return id_root.get(id);
    }

    private void createJson(){
        String baseDirPath = Configs.getInstance().getFilePath("resources/json/object/ai");
        for (int i = 2000; i < 2010; i++) {
            String name = baseDirPath + File.separator + i + ".json";
            File file = new File(name);
            if (file.exists()){
                continue;
            }
            JSONObject jsonObject = randomJSONObject(0);
            JSONObject rootObject = new JSONObject();
            rootObject.put("root", jsonObject);
            try {
                FileWriter writer = new FileWriter(name);
                writer.write(JSONObject.toJSONString(rootObject,
                        SerializerFeature.PrettyFormat,
                        SerializerFeature.WriteNullStringAsEmpty,
                        SerializerFeature.WriteNullNumberAsZero)
                );
                writer.flush();
                writer.close();
            } catch (IOException e) {
                logger.error("create {} error.", name, e);
            }
        }
    }

    private JSONObject createJSONObject(String type, int depth){
        JSONObject jsonObject = new JSONObject();
        if (type.equals("Sequence") || type.equals("Selector")){
            jsonObject.put("random", RandomUtil.randomBetween(500,1000));
            JSONArray array = new JSONArray();
            int length = RandomUtil.nextInt(5) + 1;
            for (int i = 0; i < length; i++) {
                JSONObject child = randomJSONObject(depth + 1);
                child.put("weight", RandomUtil.randomBetween(100,1000));
                array.add(child);
            }
            jsonObject.put("children", array);
        }
        else if (type.equals("Decorator")){
            JSONObject child = randomJSONObject(depth + 1);
            jsonObject.put("decorator", child);
        }
        jsonObject.put("type", type);
        return jsonObject;
    }

    private JSONObject randomJSONObject(int depth){
        String[] parents = new String[]{"Sequence", "Selector"};
        String[] children = new String[]{"FreeMove", "PrintPath", "True", "Decorator", "False"};
        if (depth < 5 && RandomUtil.nextInt(50) < 10){
            int index = RandomUtil.randomBetween(0, parents.length);
            return createJSONObject(parents[index], depth + 1);
        }
        else {
            int index = RandomUtil.randomBetween(0, children.length);
            return createJSONObject(children[index], depth + 1);
        }
    }
}
