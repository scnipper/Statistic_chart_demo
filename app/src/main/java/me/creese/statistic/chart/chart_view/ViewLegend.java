package me.creese.statistic.chart.chart_view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import java.util.ArrayList;

import me.creese.statistic.chart.chart_view.impl.Drawable;

public class ViewLegend implements Drawable {


    private static final String TAG = ViewLegend.class.getSimpleName();
    private static final float PADDING_TEXT = 33;
    private static final float SPACE_BETWEEN_VAL = 20;
    private final Paint paint;
    private final ArrayList<DataView> dataViews;
    private float width;
    private float height;
    private boolean isVisible;
    private float x;
    private float y;
    private RectF bound;
    private String headText;
    private int canvasWidth;
    private float leftWidth;
    private float rightWidth;

    public ViewLegend() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);

        bound = new RectF();

        dataViews = new ArrayList<>();


    }

    public void measureText() {
        paint.setTextSize(40);
        paint.setTypeface(Typeface.DEFAULT_BOLD);

        float headSize = paint.measureText(headText);
        width = headSize+PADDING_TEXT*2;

        leftWidth = 0;
        rightWidth = 0;


        for (int i = 0; i < dataViews.size(); i++) {
            DataView dataView = dataViews.get(i);
            String value = dataView.value;
            String name = dataView.name;

            paint.setTextSize(50);
            paint.setTypeface(Typeface.DEFAULT_BOLD);

            float valSize = paint.measureText(value);

            paint.setTypeface(Typeface.DEFAULT);
            paint.setTextSize(40);

            float nameSize = paint.measureText(name);

            float max = Math.max(valSize, nameSize);



            if (i % 2 == 0) {
                if(max > leftWidth) leftWidth = max;
            } else {
                if(max > rightWidth) rightWidth = max;
            }
        }

        float newWidth = PADDING_TEXT * 2 + leftWidth + SPACE_BETWEEN_VAL + rightWidth;

        if(newWidth > width) width = newWidth;


    }

    public void addDataView(DataView dataView) {
        dataViews.add(dataView);
    }

    public ArrayList<DataView> getDataViews() {
        return dataViews;
    }

    public void setHeadText(String headText) {
        this.headText = headText;
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;

        if(x < 0 ) this.x = 20;
        if(x +width > canvasWidth) {
            this.x = canvasWidth-width-SPACE_BETWEEN_VAL;
        }
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    @Override
    public void draw(Canvas canvas) {
        canvasWidth = canvas.getWidth();
        if (!isVisible) return;

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        bound.set(x, y, x + width, y + height);


        canvas.drawRoundRect(bound, 12, 12, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xffe7e8e9);
        canvas.drawRoundRect(bound, 12, 12, paint);
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(Color.BLACK);
        paint.setTextSize(40);

        float xText = this.x +PADDING_TEXT;
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawText(headText, xText,y+50,paint);

        float yValues = this.y + 170;
        for (int i = 0; i < dataViews.size(); i++) {
            DataView dataView = dataViews.get(i);
            float _x;
            if(i % 2 == 0) {
                _x = xText;
                if(i > 1)
                    yValues+=110;
            } else {
                _x = xText+leftWidth+SPACE_BETWEEN_VAL;
            }
            paint.setColor(dataView.color);
            paint.setTextSize(50);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            canvas.drawText(dataView.value,_x,yValues-50,paint);
            paint.setTypeface(Typeface.DEFAULT);
            paint.setTextSize(40);
            canvas.drawText(dataView.name,_x, yValues,paint);
        }

        height = (yValues-y) + SPACE_BETWEEN_VAL;


    }

    public static class DataView {
        public String name;
        public String value;
        public int color;

        public DataView(String name, String value, int color) {
            this.name = name;
            this.value = value;
            this.color = color;
        }
    }
}
