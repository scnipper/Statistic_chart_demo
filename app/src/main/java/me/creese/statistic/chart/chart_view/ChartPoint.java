package me.creese.statistic.chart.chart_view;

public class ChartPoint {
    // x value from data json
    private float x;
    // y value from data json
    private float y;
    // x value to screen coordinates
    private float normX;
    // y value to screen coordinates
    private float normY;
    // if true draw circle point
    private boolean drawCircle;


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

    public boolean isDrawCircle() {
        return drawCircle;
    }

    public void setDrawCircle(boolean drawCircle) {
        this.drawCircle = drawCircle;
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

    @Override
    public String toString() {
        return "ChartPoint{" + "x=" + x + ", y=" + y + '}';
    }
}
