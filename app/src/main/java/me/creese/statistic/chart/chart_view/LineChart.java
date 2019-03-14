package me.creese.statistic.chart.chart_view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;

import me.creese.statistic.chart.chart_view.impl.Drawable;

public class LineChart implements Drawable {
    private final Paint paint;
    private ArrayList<ChartPoint> points;
    private Path linePath;
    private Matrix matrix;


    public LineChart() {
        points = new ArrayList<>();
        matrix = new Matrix();
        matrix.reset();
        linePath = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setWidthLineChart(5);
        setColorLine(Color.RED);
        paint.setAntiAlias(true);

        paint.setStyle(Paint.Style.STROKE);
    }

    public void setColorLine(int color) {
        paint.setColor(color);
    }

    public void setWidthLineChart(float widthLine) {
        paint.setStrokeWidth(widthLine);
    }

    public void addPoint(ChartPoint point) {
        points.add(point);
        // invert y
        //point.setY(getHeight() - point.getY());

    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void normPoints(int width, int height) {

        ChartPoint max = getMaxXY();
        float normX = width/max.getX();
        float normY = height/max.getY();

        for (ChartPoint point : points) {
            point.setNormX(point.getX()*normX);
            point.setNormY(height-point.getY()*normY);
        }
    }


    private ChartPoint getMaxXY() {
        ChartPoint max = new ChartPoint();
        for (ChartPoint point : points) {
            if(point.getX() > max.getX()) max.setX(point.getX());
            if(point.getY() > max.getY()) max.setY(point.getY());
        }
        return max;
    }

    @Override
    public void draw(Canvas canvas) {



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
