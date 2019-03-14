package me.creese.statistic.chart.chart_view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import me.creese.statistic.chart.MainActivity;

public class Chart extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = Chart.class.getSimpleName();

    private float widthLineChart = 25;

    private DrawThread drawThread;
    private ArrayList<LineChart> lines;

    public Chart(Context context) {
        super(context);
        init();
    }

    public Chart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Chart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Chart(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        lines = new ArrayList<>();
        getHolder().addCallback(this);
    }



    public void addLine(LineChart lineChart) {
        lines.add(lineChart);
    }

    public ArrayList<LineChart> getLines() {
        return lines;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), (int) (MainActivity.HEIGHT_SCREEN * 0.6f));
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(this);
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawThread.setRunning(false);

        while(retry) {
            try {
                drawThread.join();
                retry = false;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
