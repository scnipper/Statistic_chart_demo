package me.creese.statistic.chart.chart_view;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

import me.creese.statistic.chart.ThemeWrapper;
import me.creese.statistic.chart.chart_view.impl.Drawable;
import me.creese.statistic.chart.chart_view.impl.LineFormatter;

public class Legend implements Drawable {

    private final Paint textPaint;
    private final LineFormatter lineFormatter;
    private final Chart chart;
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


    public Legend(Chart chart) {
        this.chart = chart;
        this.lineFormatter = chart.getLineFormatter();
        countPart = 6;
        countTmp = 6;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        XVertLine = -1;

        paint.setStrokeWidth(2);
        paint.setColor(ThemeWrapper.CHART_LINE_COLOR);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setAntiAlias(true);
        textPaint.setColor(ThemeWrapper.CHART_TEXT_COLOR);
        textPaint.setTextSize(37);

    }

    public int getCountPart() {
        return countPart;
    }

    public void setCountPart(int countPart) {
        this.countPart = countPart + 1;
        countTmp = countPart + 1;

    }

    public String getFormatY(float value) {
        String text;
        if (lineFormatter != null) {
            text = lineFormatter.getFormatY(value);
        } else {
            text = String.valueOf(value);
        }
        return text;
    }

    public String getFormatX(float value) {
        String text;
        ArrayList<LineChart> lines = chart.getLines();
        float offset = 0;
        if(lines.size() > 0) {
            offset = lines.get(0).getOffsetX();
        }
        value+= offset;
        if (lineFormatter != null) {
            text = lineFormatter.getFormatX(value);
        } else {
            text = String.valueOf(((long) value));
        }
        return text;
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

        float partHeight = height / (float) (countPart);

        if (partWidth == 0) {
            partWidth = canvas.getWidth() / (float) countTmp;
        }

        float partY = maxValueY / (countPart);
        textPaint.setTextAlign(Paint.Align.LEFT);
        // draw separate lines
        for (int i = 0; i < countPart; i++) {
            float startY = height - partHeight * i;
            int valueY = (int) (partY * i);
            String text = getFormatY(valueY);


            canvas.drawText(text, 0, startY - 15, textPaint);
            canvas.drawLine(0, startY, canvas.getWidth(), startY, paint);

        }

        // vertical line

        if (XVertLine != -1) {
            canvas.drawLine(XVertLine, 0, XVertLine, canvas.getHeight() - DrawThread.BOTTOM_PADDING_CHART, paint);
        }


        float bX = 0;
        float w = widthLine / (float) countTmp;


        if (w > (this.partWidth) * 2f) countTmp *= 2;

        if (w < (this.partWidth )) countTmp /= 2;


        for (int i = 0; i < countTmp; i++) {

            float perc = bX / widthLine;
            String text = getFormatX(perc * maxValueX).substring(5);
            float xText = bX + XLine;

            if(xText == 0) textPaint.setTextAlign(Paint.Align.LEFT);
            else textPaint.setTextAlign(Paint.Align.CENTER);




            canvas.drawText(text, xText, height + 50, textPaint);

            bX += w;
        }


    }
}
