package me.creese.statistic.chart.chart_view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.ArrayList;

public class DrawThread extends Thread {
    private static final long FRAME_TIME = 16_666_670; // ms ~ 60 fps
    private static final String TAG = DrawThread.class.getSimpleName();
    private final Chart chart;
    private final Paint paint;
    private final SizeRect sizeRect;

    private long prevTime;
    private Matrix matrix;
    private boolean running;
    private boolean pause;

    public DrawThread(Chart chart) {
        this.chart = chart;

        prevTime = System.nanoTime();

        matrix = new Matrix();
        matrix.reset();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        sizeRect = new SizeRect();


    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (running) {
            if (pause) continue;

            long time = System.nanoTime();
            long delta = time - prevTime;

            if (delta < FRAME_TIME) continue;

            prevTime = time;

            SurfaceHolder holder = chart.getHolder();
            canvas = holder.lockCanvas();
            if (canvas != null) {
                synchronized (chart) {
                    draw(canvas, delta / 1000_000_000.f);
                }

                holder.unlockCanvasAndPost(canvas);
            }


        }
    }

    private void draw(Canvas canvas, float delta) {

        canvas.drawColor(Color.WHITE);

        Paint text = new Paint();
        text.setTextSize(50);
        text.setColor(Color.BLACK);
        canvas.drawText((int) (1/delta)+" fps",0,60,text);



        ArrayList<LineChart> lines = chart.getLines();
        for (LineChart line : lines) {
            line.normPoints(canvas.getWidth(),canvas.getHeight()-216);
            line.getMatrix().setTranslate(0,0);
            line.draw(canvas);
            // small bottom lines
            line.normPoints(canvas.getWidth(),112);
            line.getMatrix().setTranslate(0,canvas.getHeight()-120);
            line.draw(canvas);
        }
        paint.setColor(0x22517da2);
        canvas.drawRect(0,canvas.getHeight()-125,canvas.getWidth(),canvas.getHeight(),paint);

        sizeRect.draw(canvas);
        //matrix.postTranslate(100 * delta, 0);


    }
}
