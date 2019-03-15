package me.creese.statistic.chart.chart_view;

import android.graphics.Canvas;
import android.graphics.Paint;

import me.creese.statistic.chart.chart_view.impl.Drawable;

public class Legend implements Drawable {

    private final Paint textPaint;
    private final SizeRect sizeRect;
    private float maxValueX;
    private float maxValueY;
    private int countPart;
    private Paint paint;
    private float XLine;
    private float widthLine;


    public Legend(SizeRect sizeRect) {
        this.sizeRect = sizeRect;
        countPart = 6;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);

        paint.setStrokeWidth(2);
        paint.setColor(0xffe7e8e9);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setAntiAlias(true);
        textPaint.setColor(0xff96a2aa);
        textPaint.setTextSize(40);
    }

    public int getCountPart() {
        return countPart;
    }

    public void setCountPart(int countPart) {
        this.countPart = countPart;
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

    @Override
    public void draw(Canvas canvas) {
        int height = canvas.getHeight() - DrawThread.BOTTOM_PADDING_CHART;

        float partHeight = height / (float)countPart;
        float partWidth = canvas.getWidth() /(float) countPart;
        float partY = maxValueY / countPart;
        float partX = ((maxValueX* (sizeRect.getWidth()/canvas.getWidth())) / countPart);
        float countX = widthLine / partX;


        for (int i = 0; i < countPart; i++) {
            float startY = height-partHeight * i;

            int valueY = (int) (partY * i);




            canvas.drawText(String.valueOf(valueY),0,startY-15,textPaint);

            canvas.drawLine(0, startY,canvas.getWidth(),startY,paint);


        }
        for (int i = 0; i < countX; i++) {
            float startX = partWidth * i;
            int valueX = (int) (partX * i);


            float xText = startX + XLine;
            if(xText > 50 && xText < canvas.getWidth()-100)
            canvas.drawText(String.valueOf(valueX), xText,height+50,textPaint);
        }


    }
}
