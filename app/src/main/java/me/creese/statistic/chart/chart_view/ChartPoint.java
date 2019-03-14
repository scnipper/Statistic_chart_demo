package me.creese.statistic.chart.chart_view;

public class ChartPoint {
    private float x;
    private float y;
    private float normX;
    private float normY;


    public ChartPoint() {
        this(0,0);
    }

    public ChartPoint(float x, float y) {
        this.x = x;
        this.y = y;


    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "ChartPoint{" + "x=" + x + ", y=" + y + '}';
    }

    public void setNormX(float normX) {
        this.normX = normX;
    }

    public float getNormX() {
        return normX;
    }

    public void setNormY(float normY) {
        this.normY = normY;
    }

    public float getNormY() {
        return normY;
    }
}