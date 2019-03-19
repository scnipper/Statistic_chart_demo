package me.creese.statistic.chart.chart_view;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;

import me.creese.statistic.chart.ThemeWrapper;
import me.creese.statistic.chart.chart_view.impl.Drawable;

public class LineChart implements Drawable {
    private static final String TAG = LineChart.class.getSimpleName();
    private static final int VELOVITY = 5000;
    private final Paint paint;
    private final Paint dotPaint;
    private ArrayList<ChartPoint> points;
    private Path linePath;
    private Matrix matrix;
    private int canvasWidth;
    private boolean isNormPoints;
    private float[] tmpValues;
    private boolean isAutoRescale;
    private boolean isVisible;
    private float[] tmpNormPoints;
    private boolean isDrawPointCircle;
    private float offsetX;
    private String name;
    private float delta;
    private boolean isStartHideAnim;
    private int canvasHeight;
    private boolean isStartShowAnim;


    public LineChart() {
        points = new ArrayList<>();
        matrix = new Matrix();
        matrix.reset();
        canvasWidth = -1;
        linePath = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setWidthLineChart(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        tmpValues = new float[9];

        dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setAntiAlias(true);
        dotPaint.setStyle(Paint.Style.FILL);
        dotPaint.setStrokeWidth(6);
        dotPaint.setColor(ThemeWrapper.CONTAINER_COLOR);

        isVisible = true;
    }

    public void setColorLine(int color) {
        paint.setColor(color);
    }

    public void setWidthLineChart(float widthLine) {
        paint.setStrokeWidth(widthLine);
    }

    public void addPoint(ChartPoint point) {
        if (points.size() == 0) {
            offsetX = point.getX();
        }
        point.setX(point.getX() - offsetX);
        points.add(point);

    }

    public void normPoints(int width, int height, ChartPoint max) {

        float normX = (width) / max.getX();

        float normY = height / max.getY();


        for (ChartPoint point : points) {
            point.setNormX(point.getX() * normX);
            point.setNormY(height - point.getY() * normY);
        }
        isNormPoints = true;
    }


    public ChartPoint getMaxXY() {
        ChartPoint max = new ChartPoint(1, 1);
        for (ChartPoint point : points) {


            if (point.getX() > max.getX()) max.setX(point.getX());
            if (isNormPoints && isAutoRescale) {


                float transX = getTranslateX();

                float _x = point.getNormX() + transX;
                if (_x > canvasWidth || _x < 0) continue;
            }
            if (point.getY() > max.getY()) max.setY(point.getY());
        }
        return max;
    }

    public void hide() {

        isStartHideAnim = true;
        matrix.setTranslate(getTranslateX(), 0);
    }

    public void show() {

        matrix.setTranslate(getTranslateX(), -canvasHeight);
        isStartShowAnim = true;
        isVisible = true;

    }

    public void restoreNormPoints() {
        for (int i = 0; i < points.size(); i++) {
            ChartPoint point = points.get(i);

            point.setNormX(tmpNormPoints[i]);
            point.setNormY(tmpNormPoints[i + 1]);
        }
    }

    public void saveNormPoint() {
        if (tmpNormPoints == null) {
            tmpNormPoints = new float[points.size() * 2];
        }

        for (int i = 0; i < points.size(); i++) {
            ChartPoint point = points.get(i);
            tmpNormPoints[i] = point.getNormX();
            tmpNormPoints[i + 1] = point.getNormY();
        }
    }

    public float getTranslateX() {

        matrix.getValues(tmpValues);
        return tmpValues[Matrix.MTRANS_X];

    }

    public float getTranslateY() {
        matrix.getValues(tmpValues);
        return tmpValues[Matrix.MTRANS_Y];
    }

    public void setAutoRescale(boolean autoRescale) {
        isAutoRescale = autoRescale;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public ArrayList<ChartPoint> getPoints() {
        return points;
    }

    public void setDrawPointCircle(boolean drawPointCircle) {
        isDrawPointCircle = drawPointCircle;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public boolean isStartHideAnim() {
        return isStartHideAnim;
    }

    public boolean isStartShowAnim() {
        return isStartShowAnim;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return paint.getColor();
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    @Override
    public void draw(Canvas canvas) {
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getWidth();
        if (!isVisible) return;


        linePath.reset();
        ChartPoint prePoint = null;
        for (int i = 1; i < points.size(); i++) {

            ChartPoint point = points.get(i);

            float translateX = getTranslateX();
            if (point.getNormX() + translateX < 0) continue;

            if (prePoint == null) {
                prePoint = points.get(i - 1);
                linePath.moveTo(prePoint.getNormX(), prePoint.getNormY());
            }
            linePath.lineTo(point.getNormX(), point.getNormY());


            if (point.getNormX() + translateX > canvasWidth) break;

        }


        linePath.transform(matrix);
        canvas.drawPath(linePath, paint);

        if (isStartHideAnim) {
            matrix.preTranslate(0, -VELOVITY * delta);

            float translateY = getTranslateY();


            if (translateY < -canvas.getHeight()) {
                isStartHideAnim = false;

                isVisible = false;
            }

        }

        if (isStartShowAnim) {
            matrix.preTranslate(0, VELOVITY * delta);

            float translateY = getTranslateY();


            if (translateY >= 0) {
                isStartShowAnim = false;
            }
        }

        if (isDrawPointCircle) {

            float translateX = getTranslateX();
            for (ChartPoint point : points) {
                if (point.isDrawCircle()) {
                    canvas.drawCircle(point.getNormX() + translateX, point.getNormY(), 15, dotPaint);
                    canvas.drawCircle(point.getNormX() + translateX, point.getNormY(), 15, paint);
                }
            }
        }


    }


}
