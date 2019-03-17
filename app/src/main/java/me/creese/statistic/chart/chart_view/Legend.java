package me.creese.statistic.chart.chart_view;

import android.graphics.Canvas;
import android.graphics.Paint;

import me.creese.statistic.chart.chart_view.impl.Drawable;
import me.creese.statistic.chart.chart_view.impl.LineFormatter;

public class Legend implements Drawable {

    private final Paint textPaint;
    private final LineFormatter lineFormatter;
    private float maxValueX;
    private float maxValueY;
    private int countPart;
    private Paint paint;
    private float XLine;
    private float widthLine;
    private float XVertLine;
    private int countTmp;
    private float partWidth;
    private float apendPartWidth;


    public Legend(LineFormatter lineFormatter) {
        this.lineFormatter = lineFormatter;
        countPart = 6;
        countTmp = 6;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        XVertLine = -1;

        paint.setStrokeWidth(2);
        paint.setColor(0xffe7e8e9);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setAntiAlias(true);
        textPaint.setColor(0xff96a2aa);
        textPaint.setTextSize(37);

    }

    public int getCountPart() {
        return countPart;
    }

    public void setCountPart(int countPart) {
        this.countPart = countPart + 1;
        countTmp = countPart + 1;

    }

    public void setMaxValueX(float maxValueX) {
        this.maxValueX = maxValueX;
    }

    public void setMaxValueY(float maxValueY) {
        this.maxValueY = maxValueY;
    }


    public void setXLine(float xLine) {
        this.XLine = xLine;
    }

    public void setWidthLine(int widthLine) {
        this.widthLine = widthLine;
    }

    public void setXVertLine(float xVertLine) {
        this.XVertLine = xVertLine;
    }

    @Override
    public void draw(Canvas canvas) {
        int height = canvas.getHeight() - DrawThread.BOTTOM_PADDING_CHART;

        float partHeight = height / (float) (countPart - 1);

        if (partWidth == 0) {
            partWidth = canvas.getWidth() / (float) countTmp;
        }

        float partY = maxValueY / (countPart - 1);
        textPaint.setTextAlign(Paint.Align.LEFT);
        // draw separate lines
        for (int i = 0; i < countPart - 1; i++) {
            float startY = height - partHeight * i;
            int valueY = (int) (partY * i);
            String text = "";
            if (lineFormatter != null) {
                text = lineFormatter.getFormatY(valueY);
            } else {
                text = String.valueOf(valueY);
            }

            canvas.drawText(text, 0, startY - 15, textPaint);
            canvas.drawLine(0, startY, canvas.getWidth(), startY, paint);

        }

        // vertical line

        if (XVertLine != -1) {
            canvas.drawLine(XVertLine, 0, XVertLine, canvas.getHeight() - DrawThread.BOTTOM_PADDING_CHART, paint);
        }

        textPaint.setTextAlign(Paint.Align.CENTER);
        float bX = 0;
        float w = widthLine / (float) countTmp;


        if (w > (this.partWidth+apendPartWidth) * 2f) countTmp *= 2;

        if (w < (this.partWidth + apendPartWidth)) countTmp /= 2;


        for (int i = 0; i < countTmp; i++) {

            float perc = bX / widthLine;
            String text = "";
            float xText = bX + XLine;

            if (lineFormatter != null) {
                text = lineFormatter.getFormatX(perc * maxValueX);
            } else {
                text = String.valueOf(((long) (perc * maxValueX)));
            }

            float measureText = textPaint.measureText(text)/4;

            if(measureText > apendPartWidth) apendPartWidth = measureText;

            canvas.drawText(text, xText, height + 50, textPaint);

            bX += w;
        }


    }
}
