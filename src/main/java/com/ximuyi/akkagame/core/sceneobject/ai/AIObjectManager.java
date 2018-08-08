package com.ximuyi.akkagame.core.sceneobject.ai;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.ximuyi.akkagame.core.bt.BTManager;
import com.ximuyi.akkagame.core.bt.BTNode;
import com.ximuyi.akkagame.core.sceneobject.ai.action.AIObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

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
        String baseDirPath = System.getProperty("user.dir") + File.separator + "resources" + File.separator + "json"
                + File.separator + "ai_object";
        File baseDir = new File(baseDirPath);
        JSONObject jsonObjet = new JSONObject();
        jsonObjet.put("type", "Selector");
        jsonObjet.put("random", 1000);
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("type", "RandomMove");
        object.put("weight", 100);
        array.add(object);
        object = new JSONObject();
        object.put("type", "True");
        object.put("weight", 100);
        array.add(object);
        JSONObject decorator = new JSONObject();
        decorator.put("type", "Decorator");
        decorator.put("weight", 100);
        object = new JSONObject();
        object.put("type", "False");
        decorator.put("decorator", object);
        array.add(decorator);
        jsonObjet.put("children", array);
        JSONObject jsonRoot = new JSONObject();
        jsonRoot.put("root", jsonObjet);
        File jsonfile = new File(baseDirPath + File.separator + "1.json");
        try {
            if (!jsonfile.exists()){
                jsonfile.createNewFile();
            }
            FileWriter writer = new FileWriter(jsonfile);
            writer.write(JSONObject.toJSONString(jsonRoot, SerializerFeature.PrettyFormat,
                                                 SerializerFeature.WriteNullStringAsEmpty,
                                                 SerializerFeature.WriteNullNumberAsZero)
            );
            writer.flush();
            writer.close();
        } catch (IOException e) {
        }
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
}
