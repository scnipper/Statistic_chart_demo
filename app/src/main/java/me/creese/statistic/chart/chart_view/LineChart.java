package me.creese.statistic.chart.chart_view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import java.util.ArrayList;

import me.creese.statistic.chart.chart_view.impl.Drawable;

public class LineChart implements Drawable {
    private static final String TAG = LineChart.class.getSimpleName();
    private final Paint paint;
    private ArrayList<ChartPoint> points;
    private Path linePath;
    private Matrix matrix;
    private SizeRect sizeRect;
    private int canvasWidth;
    private boolean isNormPoints;
    private float[] tmpValues;
    private boolean isAutoRescale;
    private boolean isVisible;
    private float[] tmpNormPoints;


    public LineChart() {
        points = new ArrayList<>();
        matrix = new Matrix();
        matrix.reset();
        canvasWidth = -1;
        linePath = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setWidthLineChart(5);
        setColorLine(Color.RED);
        paint.setAntiAlias(true);
        tmpValues = new float[9];
        paint.setStyle(Paint.Style.STROKE);
        isVisible = true;
    }

    public void setColorLine(int color) {
        paint.setColor(color);
    }

    public void setWidthLineChart(float widthLine) {
        paint.setStrokeWidth(widthLine);
    }

    public void addPoint(ChartPoint point) {
        points.add(point);

    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void normPoints(int width, int height) {


        ChartPoint max = getMaxXY();
        float normX = width / max.getX();

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
            if(isNormPoints && isAutoRescale) {

                matrix.getValues(tmpValues);

                float transX = tmpValues[Matrix.MTRANS_X];

                float _x = point.getNormX() + transX;
                if(_x > canvasWidth) continue;
            }
            if (point.getY() > max.getY()) max.setY(point.getY());
        }
        return max;
    }

    public void restoreNormPoints() {
        for (int i = 0; i < points.size(); i++) {
            ChartPoint point = points.get(i);

            point.setNormX(tmpNormPoints[i]);
            point.setNormY(tmpNormPoints[i+1]);
        }
    }
    public void saveNormPoint() {
        if (tmpNormPoints == null) {
            tmpNormPoints = new float[points.size() * 2];
        }

        for (int i = 0; i < points.size(); i++) {
            ChartPoint point = points.get(i);
            tmpNormPoints[i] = point.getNormX();
            tmpNormPoints[i+1] = point.getNormY();
        }
    }

    public void setSizeRect(SizeRect sizeRect) {
        this.sizeRect = sizeRect;
    }

    public void setAutoRescale(boolean autoRescale) {
        isAutoRescale = autoRescale;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void draw(Canvas canvas) {

        if(!isVisible) return;

        canvasWidth = canvas.getWidth();

        //Log.w(TAG, "draw: " + points.get(1).getNormX() + " " + canvasWidth);

        linePath.reset();
        for (int i = 1; i < points.size(); i++) {
            ChartPoint prePoint = points.get(i - 1);
            ChartPoint point = points.get(i);

            if (i == 1) linePath.moveTo(prePoint.getNormX(), prePoint.getNormY());
            linePath.lineTo(point.getNormX(), point.getNormY());


        }
        linePath.transform(matrix);
        canvas.drawPath(linePath, paint);

    }
}
