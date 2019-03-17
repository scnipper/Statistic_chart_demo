package me.creese.statistic.chart.jsonget;

import java.util.LinkedHashMap;

public class JsonObject extends JsonEntity {

    private final LinkedHashMap<String, JsonEntity> fields;

    public JsonObject() {
        super(null);

        fields = new LinkedHashMap<>();
    }

    public JsonEntity get(String nameField) {
        JsonEntity jsonEntity = fields.get(nameField);
        if(jsonEntity == null) throw new JsonGExeption("Dont find field \""+nameField+"\"");
        return jsonEntity;
    }
    public void addField(String name,JsonEntity value) {
        fields.put(name,value);
    }
}
