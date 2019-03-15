package me.creese.statistic.chart.chart_view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.ArrayList;

import me.creese.statistic.chart.chart_view.impl.Drawable;

public class DrawThread extends Thread implements Drawable {
    private static final long FRAME_TIME = 16_666_670; // ms ~ 60 fps
    private static final String TAG = DrawThread.class.getSimpleName();
    public static final int BOTTOM_PADDING = 125;
    public static final int BOTTOM_PADDING_CHART = 216;
    private final Chart chart;
    private final Paint paint;
    private final SizeRect sizeRect;
    private final Legend legend;

    private long prevTime;
    private Matrix matrix;
    private boolean running;
    private boolean pause;
    private float delta;

    public DrawThread(Chart chart) {
        this.chart = chart;

        prevTime = System.nanoTime();

        matrix = new Matrix();
        matrix.reset();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0x88517da2);
        sizeRect = new SizeRect();
        legend = new Legend(sizeRect);


    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public SizeRect getSizeRect() {
        return sizeRect;
    }

    public float getDelta() {
        return delta;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (running) {
            if (pause) continue;

            long time = System.nanoTime();
            long elapsed = time - prevTime;

            if (elapsed < FRAME_TIME) continue;

            prevTime = time;

            SurfaceHolder holder = chart.getHolder();
            canvas = holder.lockCanvas();
            if (canvas != null) {
                synchronized (chart) {
                    delta = elapsed / 1000_000_000.f;
                    draw(canvas);
                }

                holder.unlockCanvasAndPost(canvas);
            }


        }
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawColor(Color.WHITE);

        Paint text = new Paint();
        text.setTextSize(50);
        text.setColor(Color.BLACK);
        canvas.drawText((int) (1/delta)+" fps",0,60,text);


        ArrayList<LineChart> lines = chart.getLines();
        int widthLine = (int) (canvas.getWidth() * (canvas.getWidth()/sizeRect.getWidth()));
        float xLine = -sizeRect.getX() * (widthLine / (float) canvas.getWidth());

        if(lines.size() > 0) {
            LineChart lineChart = lines.get(0);
            lineChart.setAutoRescale(true);
            ChartPoint maxXY = lineChart.getMaxXY();
            legend.setMaxValueX(maxXY.getX());
            legend.setMaxValueY(maxXY.getY());
            legend.setXLine(xLine);
            legend.setWidthLine(widthLine);
        }
        legend.draw(canvas);
        for (LineChart line : lines) {
            int height = canvas.getHeight() - BOTTOM_PADDING_CHART;
            // small bottom lines
            line.saveNormPoint();
            line.setAutoRescale(false);
            line.getMatrix().setTranslate(0,canvas.getHeight()-120);
            line.normPoints( canvas.getWidth(),112);
            line.draw(canvas);

            line.restoreNormPoints();

            line.setAutoRescale(true);
            line.getMatrix().setTranslate(xLine,0);
            line.normPoints(widthLine, height);
            line.draw(canvas);


        }

        canvas.drawRect(0,canvas.getHeight()-BOTTOM_PADDING,sizeRect.getX(),canvas.getHeight(),paint);
        canvas.drawRect(sizeRect.getX()+sizeRect.getWidth(),canvas.getHeight()-BOTTOM_PADDING,canvas.getWidth(),canvas.getHeight(),paint);

        sizeRect.draw(canvas);
        //matrix.postTranslate(100 * delta, 0);


    }
}
