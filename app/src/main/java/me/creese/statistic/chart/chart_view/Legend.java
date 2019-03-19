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
    // max value x from json data
    private float maxValueX;
    // max value y from json data
    private float maxValueY;
    private Paint paint;
    // x position lines
    private float XLine;
    // width chart line in screen coordinates
    private float widthLine;
    // position draw vertical line
    private float XVertLine;
    // count part divide screen
    private int countPart;
    private int countTmp;
    private float partWidth;


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

    /**
     * Return format value y. Use {@link LineFormatter} or just return return raw string
     * @param value
     * @return
     */
    public String getFormatY(float value) {
        String text;
        if (lineFormatter != null) {
            text = lineFormatter.getFormatY(value);
        } else {
            text = String.valueOf(value);
        }
        return text;
    }

    /**
     * Return format value x. Use {@link LineFormatter} or just return return raw string
     * @param value
     * @return
     */
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

    /**
     * Draw grid of chart
     * @param canvas
     */
    public void drawGrid(Canvas canvas) {
        textPaint.setTextAlign(Paint.Align.LEFT);
        int height = canvas.getHeight() - DrawThread.BOTTOM_PADDING_CHART;
        float partHeight = height / (float) (countPart);

        // draw separate lines
        for (int i = 0; i < countPart; i++) {
            float startY = height - partHeight * i;
            canvas.drawLine(0, startY, canvas.getWidth(), startY, paint);
        }

        // vertical line

        if (XVertLine != -1) {
            canvas.drawLine(XVertLine, 0, XVertLine, canvas.getHeight() - DrawThread.BOTTOM_PADDING_CHART, paint);
        }
    }
    @Override
    public void draw(Canvas canvas) {
        int height = canvas.getHeight() - DrawThread.BOTTOM_PADDING_CHART;



        if (partWidth == 0) {
            partWidth = canvas.getWidth() / (float) countTmp;
        }

        float partHeight = height / (float) (countPart);
        float partY = maxValueY / (countPart);
        for (int i = 0; i < countPart; i++) {
            float startY = height - partHeight * i;
            int valueY = (int) (partY * i);
            String text = getFormatY(valueY);
            canvas.drawText(text, 0, startY - 15, textPaint);
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
