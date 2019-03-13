package me.creese.statistic.chart.jsonget;

import java.util.LinkedHashMap;

public class JsonObject extends JsonEntity {

    private final LinkedHashMap<String, JsonEntity> fields;

    public JsonObject() {
        super(null);

        fields = new LinkedHashMap<>();
    }

    public void addField(String name,JsonEntity value) {
        fields.put(name,value);
    }
}
