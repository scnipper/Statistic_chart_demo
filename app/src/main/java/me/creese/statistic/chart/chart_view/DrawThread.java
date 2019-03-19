package me.creese.statistic.chart.chart_view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.ArrayList;

import me.creese.statistic.chart.ThemeWrapper;
import me.creese.statistic.chart.chart_view.impl.Drawable;

public class DrawThread extends Thread implements Drawable {
    public static final int BOTTOM_PADDING = 125;
    public static final int BOTTOM_PADDING_CHART = 216;
    public static final int FRAME_TO_PAUSE_RENDER = 60;
    private static final long FRAME_TIME = 16_666_670; // nano ~ 60 fps
    private static final String TAG = DrawThread.class.getSimpleName();
    private final Chart chart;
    private final Paint paint;
    private final SizeRect sizeRect;
    private final Legend legend;
    private final ViewLegend viewLegend;

    private long prevTime;
    private boolean running;
    private boolean pause;
    // time between last frame and current frame in seconds
    private float delta;
    private int frameToPause;

    public DrawThread(Chart chart) {
        super("Render");
        this.chart = chart;

        clearTime();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(ThemeWrapper.BOTTOM_SQUARES_COLOR);
        sizeRect = new SizeRect();
        legend = new Legend(chart);
        viewLegend = new ViewLegend();


    }

    /**
     * Set actual time
     */
    public void clearTime() {
        prevTime = System.nanoTime();
    }

    /**
     * Return max value x and y ChartPoint between all lines
     * @param isRescale if true max value only screen coord
     * @return
     */
    public ChartPoint getMaxValueLines(boolean isRescale) {
        float maxX = 0;
        float maxY = 0;

        for (LineChart line : chart.getLines()) {
            if (line.isVisible()) {
                line.setAutoRescale(isRescale);
                ChartPoint maxXY = line.getMaxXY();

                if (maxXY.getX() > maxX) maxX = maxXY.getX();
                if (maxXY.getY() > maxY) maxY = maxXY.getY();
            }
        }
        return new ChartPoint(maxX, maxY);

    }

    /**
     * Render stops after a while "FRAME_TO_PAUSE_RENDER". This method will resume render.
     */
    public void requestRender() {
        pause = false;
        frameToPause = 0;
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


    public Legend getLegend() {
        return legend;
    }

    public ViewLegend getViewLegend() {
        return viewLegend;
    }

    /**
     * Main loop drawing
     */
    @Override
    public void run() {
        while (running) {
            if (pause) continue;

            long time = System.nanoTime();
            long elapsed = time - prevTime;

            if (elapsed < FRAME_TIME) continue;

            prevTime = time;

            SurfaceHolder holder = chart.getHolder();
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                synchronized (chart) {
                    delta = elapsed / 1000_000_000.f;
                    draw(canvas);
                }

                holder.unlockCanvasAndPost(canvas);

                frameToPause++;

                if (frameToPause >= FRAME_TO_PAUSE_RENDER) pause = true;
            }


        }
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawColor(ThemeWrapper.CONTAINER_COLOR);

        /*Paint text = new Paint();
        text.setTextSize(50);
        text.setColor(Color.BLACK);
        canvas.drawText((int) (1 / delta) + " fps", 0, 60, text);*/


        ArrayList<LineChart> lines = chart.getLines();
        int widthLine = (int) (canvas.getWidth() * (canvas.getWidth() / sizeRect.getWidth()));
        float xLine = -sizeRect.getX() * (widthLine / (float) canvas.getWidth());


        ChartPoint maxXY = getMaxValueLines(true);
        ChartPoint maxXYNotRescale = getMaxValueLines(false);
        legend.setMaxValueX(maxXY.getX());
        legend.setMaxValueY(maxXY.getY());
        legend.setXLine(xLine);
        legend.setWidthLine(widthLine);

        legend.drawGrid(canvas);
        for (int i = 0; i < lines.size(); i++) {
            LineChart line = lines.get(i);

            line.setDelta(delta);
            int height = canvas.getHeight() - BOTTOM_PADDING_CHART;
            // small bottom lines
            if (!line.isStartHideAnim() && !line.isStartShowAnim()) {
                line.setDrawPointCircle(false);
                line.saveNormPoint();

                line.getMatrix().setTranslate(0, canvas.getHeight() - 120);

                line.normPoints(canvas.getWidth(), 112, maxXYNotRescale);
                line.draw(canvas);
                line.restoreNormPoints();
            }
            // main lines
            line.setDrawPointCircle(true);
            if (!line.isStartHideAnim() && !line.isStartShowAnim()) {
                line.getMatrix().setTranslate(xLine, 0);
            }
            line.normPoints(widthLine, height, maxXY);
            line.draw(canvas);


        }

        legend.draw(canvas);
        canvas.drawRect(0, canvas.getHeight() - BOTTOM_PADDING, sizeRect.getX(), canvas.getHeight(), paint);
        canvas.drawRect(sizeRect.getX() + sizeRect.getWidth(), canvas.getHeight() - BOTTOM_PADDING, canvas.getWidth(), canvas.getHeight(), paint);

        sizeRect.draw(canvas);

        viewLegend.draw(canvas);


    }
}
