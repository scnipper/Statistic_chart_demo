package me.creese.statistic.chart.jsonget;

import java.util.ArrayList;

public class JsonArray extends JsonEntity {

    private final ArrayList<JsonEntity> array;

    public JsonArray() {
        super(null);
        array = new ArrayList<>();
    }

    public JsonEntity get(int index) {
        return array.get(index);
    }
    public void add(JsonEntity jsonEntity) {
        array.add(jsonEntity);
    }
    public int size() {
        return array.size();
    }
}
