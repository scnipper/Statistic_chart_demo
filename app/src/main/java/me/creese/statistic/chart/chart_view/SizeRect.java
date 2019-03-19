package me.creese.statistic.chart.chart_view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import me.creese.statistic.chart.ThemeWrapper;
import me.creese.statistic.chart.chart_view.impl.Drawable;

public class SizeRect implements Drawable {

    private final Paint paint;
    private final int widthLeftAndRightLines;
    private float width;
    private float height;
    private float x;
    private float y;
    private int widthCanvas = -1;
    private int heightCanvas = -1;

    public SizeRect() {

        widthLeftAndRightLines = 12;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(ThemeWrapper.SIZE_RECT_COLOR);
        paint.setStrokeWidth(5);
        width = 200;
        height = 125;
        x = 0;
        y = 0;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        if (x < 0) this.x = 0;
        if (x > widthCanvas - width) {
            this.x = widthCanvas - width;
        }
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
        if (width < 50) this.width = 50;
    }

    public void setLeft(float left) {


        float tmpX = x;
        if(width > 50)
        x = left;
        setWidth(width + (tmpX - left));


    }

    public boolean hitRightLine(float x, float y) {
        return x > this.x + (width - widthLeftAndRightLines) && x < this.x + width + 20 && y > heightCanvas - height && y < heightCanvas - this.y;
    }

    public boolean hitLeftLine(float x, float y) {
        return x > this.x - 20 && x < this.x + widthLeftAndRightLines && y > heightCanvas - height && y < heightCanvas - this.y;
    }

    public boolean hit(float x, float y) {
        return x > this.x + widthLeftAndRightLines && x < (this.x + width) - widthLeftAndRightLines && y > heightCanvas - height && y < heightCanvas - this.y;
    }

    @Override
    public void draw(Canvas canvas) {
        if (widthCanvas == -1 || heightCanvas == -1) {
            widthCanvas = canvas.getWidth();
            heightCanvas = canvas.getHeight();
        }
        float _y = canvas.getHeight() - y;
        canvas.drawRect(x, canvas.getHeight()-2.5f, x + widthLeftAndRightLines, canvas.getHeight() - height+2.5f, paint);
        canvas.drawRect(x + width - widthLeftAndRightLines, canvas.getHeight()-2.5f, x + width, canvas.getHeight() - height+2.5f, paint);

        canvas.drawLine(x, _y - 2.5f, x + width, _y - 2.5f, paint);
        canvas.drawLine(x, _y - height + 2.5f, x + width, _y - height + 2.5f, paint);
    }
}
