package me.creese.statistic.chart.custom_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import me.creese.statistic.chart.MainActivity;

public class Chart extends View {
    private static final String TAG = Chart.class.getSimpleName();

    public Chart(Context context) {
        super(context);
    }

    public Chart(Context context,  @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Chart(Context context,  @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Chart(Context context,  @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(getMeasuredWidth(), (int) (MainActivity.HEIGHT_SCREEN*0.6f));




    }

    @SuppressLint("Range")
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Paint paint = new Paint();
        paint.setColor(0xffffff00);
        paint.setTextSize(100);
        paint.setStrokeWidth(10);
        canvas.drawColor(Color.GREEN);

        paint.setStyle(Paint.Style.FILL);
        /*canvas.drawLine(0,0,500,500,paint);
        canvas.drawLine(500,500,500,0,paint);*/

        float[] lines = new float[]{
                0,0,
                500,500,
                500,500,
                500,0
        };
        canvas.drawLines(lines,paint);

        canvas.drawText("lol",100,100,paint);

    }
}
