package me.creese.statistic.chart.chart_view;

import android.graphics.Canvas;
import android.graphics.Matrix;

import me.creese.statistic.chart.chart_view.impl.Drawable;

public class SizeRect implements Drawable {

    private final Matrix matrix;

    public SizeRect() {
        matrix = new Matrix();
        matrix.reset();
    }

    public Matrix getMatrix() {
        return matrix;
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
