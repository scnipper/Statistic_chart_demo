package me.creese.statistic.chart.chart_view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import me.creese.statistic.chart.MainActivity;
import me.creese.statistic.chart.chart_view.impl.LineFormatter;

public class Chart extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = Chart.class.getSimpleName();

    private DrawThread drawThread;
    private ArrayList<LineChart> lines;
    private boolean isHitInSizeRect;
    private float downX;
    private boolean isHitLeftLine;
    private boolean isHitRightLine;
    private ArrayList<ChartPoint> circlePoints;
    private LineFormatter lineFormatter;


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
        circlePoints = new ArrayList<>();
        getHolder().addCallback(this);
    }

    public void clear() {
        hideViewLegend();
        lines.clear();
    }

    public void addLine(LineChart lineChart) {
        lines.add(lineChart);
    }

    public ArrayList<LineChart> getLines() {
        return lines;
    }

    public void setLineFormatter(LineFormatter lineFormatter) {
        this.lineFormatter = lineFormatter;
    }

    public LineFormatter getLineFormatter() {
        return lineFormatter;
    }

    public DrawThread getDrawThread() {
        return drawThread;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int actionMasked = event.getActionMasked();

        SizeRect sizeRect = drawThread.getSizeRect();


        switch (actionMasked) {
            case MotionEvent.ACTION_UP:
                isHitInSizeRect = false;
                isHitRightLine = false;
                isHitLeftLine = false;
                break;
            case MotionEvent.ACTION_DOWN:
                drawThread.requestRender();
                downX = event.getX() - sizeRect.getX();

                float y = event.getY();
                float x = event.getX();
                ViewLegend viewLegend = drawThread.getViewLegend();

                hideViewLegend();
                float normX=0;
                if (y < getHeight() - DrawThread.BOTTOM_PADDING_CHART) {

                    for (LineChart line : lines) {
                        ArrayList<ChartPoint> points = line.getPoints();
                        for (ChartPoint point : points) {
                            normX = point.getNormX() + line.getTranslateX();
                            if (normX < getWidth()) {
                                if (x > normX - 15 && x < normX + 15 && line.isVisible()) {
                                    point.setDrawCircle(true);
                                    circlePoints.add(point);
                                    Legend legend = drawThread.getLegend();
                                    legend.setXVertLine( normX);

                                    String headText = legend.getFormatX(point.getX());
                                    String value = legend.getFormatY(point.getY());
                                    viewLegend.setHeadText(headText);
                                    viewLegend.addDataView(new ViewLegend.DataView(line.getName(),value,line.getColor()));



                                    break;
                                }
                            } else break;
                        }
                    }

                    if(viewLegend.getDataViews().size() > 0) {
                        viewLegend.measureText();
                        viewLegend.setVisible(true);
                        viewLegend.setX(normX - 70);
                        viewLegend.setY(100);
                    }

                    break;
                }

                if (sizeRect.hit(event.getX(), event.getY())) {
                    isHitInSizeRect = true;
                } else if (sizeRect.hitLeftLine(event.getX(), event.getY())) {
                    isHitLeftLine = true;
                } else if (sizeRect.hitRightLine(event.getX(), event.getY())) {
                    isHitRightLine = true;
                }
            case MotionEvent.ACTION_MOVE:
                drawThread.requestRender();
                if (isHitInSizeRect) {
                    sizeRect.setX(event.getX() - downX);
                } else if (isHitRightLine) {
                    sizeRect.setWidth(event.getX() - sizeRect.getX());
                } else if (isHitLeftLine) {
                    sizeRect.setLeft(event.getX());
                }
                break;

        }

        return true;
    }

    public void hideViewLegend() {
        drawThread.getLegend().setXVertLine(-1);
        ViewLegend viewLegend = drawThread.getViewLegend();
        viewLegend.setVisible(false);
        viewLegend.getDataViews().clear();
        if (circlePoints.size() > 0) {
            for (ChartPoint circlePoint : circlePoints) {
                circlePoint.setDrawCircle(false);
            }
            circlePoints.clear();
        }
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
        holder.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawThread.setRunning(false);

        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
