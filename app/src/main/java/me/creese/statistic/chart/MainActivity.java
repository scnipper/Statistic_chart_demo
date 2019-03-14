package me.creese.statistic.chart;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

import java.io.DataInputStream;
import java.io.IOException;

import me.creese.statistic.chart.chart_view.Chart;
import me.creese.statistic.chart.chart_view.ChartPoint;
import me.creese.statistic.chart.chart_view.LineChart;
import me.creese.statistic.chart.jsonget.JsonG;

public class MainActivity extends AppCompatActivity {

    public static float WIDTH_SCREEN;
    public static float HEIGHT_SCREEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WIDTH_SCREEN = size.x;
        HEIGHT_SCREEN = size.y;


        try {
            DataInputStream stream = new DataInputStream(getResources().getAssets().open("chart_data.json"));

            byte bytes[] = new byte[stream.available()];
            stream.readFully(bytes);

            String fullJson = new String(bytes);

            JsonG jsonG = new JsonG();


            //JsonEntity jsonEntity = jsonG.generateRoot(fullJson);



        } catch (IOException e) {
            e.printStackTrace();
        }

        final Chart chart = findViewById(R.id.stat_chart);


        chart.post(new Runnable() {
            @Override
            public void run() {
                LineChart lineChart = new LineChart();
                lineChart.addPoint(new ChartPoint(0,150));
                lineChart.addPoint(new ChartPoint(100,230));
                lineChart.addPoint(new ChartPoint(160,0));
                lineChart.addPoint(new ChartPoint(200,170));
                lineChart.addPoint(new ChartPoint(260,200));
                lineChart.addPoint(new ChartPoint(360,2000));
                lineChart.setColorLine(Color.BLACK);
                chart.addLine(lineChart);
            }
        });


    }
}
