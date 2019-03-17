package me.creese.statistic.chart.jsonget;

public class JsonEntity {
    private String value;

    public JsonEntity(String value) {
        this.value = value;
    }

    public int getAsInteger() {
        return Integer.valueOf(value);
    }

    public float getAsFloat() {
        return Float.valueOf(value);
    }
    public long getAsLong() {
        return Long.valueOf(value);
    }
    public String getAsString() {
        return value;
    }

    public JsonArray getAsArray() {
        return (JsonArray) this;
    }
    public JsonObject getAsObject() {
        return (JsonObject) this;
    }
}
