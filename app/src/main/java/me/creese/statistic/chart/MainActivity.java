package me.creese.statistic.chart;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

import java.io.DataInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.creese.statistic.chart.chart_view.Chart;
import me.creese.statistic.chart.chart_view.ChartPoint;
import me.creese.statistic.chart.chart_view.LineChart;
import me.creese.statistic.chart.chart_view.impl.LineFormatter;
import me.creese.statistic.chart.jsonget.JsonArray;
import me.creese.statistic.chart.jsonget.JsonEntity;
import me.creese.statistic.chart.jsonget.JsonG;
import me.creese.statistic.chart.jsonget.JsonGExeption;
import me.creese.statistic.chart.jsonget.JsonObject;

public class MainActivity extends AppCompatActivity implements LineFormatter {

    public static float HEIGHT_SCREEN;
    private Chart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        HEIGHT_SCREEN = size.y;


        chart = findViewById(R.id.stat_chart);
        chart.setLineFormatter(this);
        try {
            DataInputStream stream = new DataInputStream(getResources().getAssets().open("chart_data.json"));

            byte bytes[] = new byte[stream.available()];
            stream.readFully(bytes);

            String fullJson = new String(bytes);

            JsonG jsonG = new JsonG();

            makeCharts(jsonG.generateRoot(fullJson));


        } catch (IOException e) {
            e.printStackTrace();
        }




        /*LineChart lineChart = new LineChart();
        lineChart.setName("hi");
        lineChart.addPoint(new ChartPoint(0,150));
        lineChart.addPoint(new ChartPoint(100,230));
        lineChart.addPoint(new ChartPoint(160,0));
        lineChart.addPoint(new ChartPoint(200,170));
        lineChart.addPoint(new ChartPoint(260,200));
        lineChart.addPoint(new ChartPoint(360,2000));
        lineChart.addPoint(new ChartPoint(500,150));
        lineChart.addPoint(new ChartPoint(560,1500));
        lineChart.addPoint(new ChartPoint(660,2000));
        lineChart.addPoint(new ChartPoint(730,900));
        lineChart.addPoint(new ChartPoint(800,300));
        lineChart.addPoint(new ChartPoint(960,1800));
        lineChart.addPoint(new ChartPoint(1160,250));
        lineChart.setColorLine(Color.BLACK);
        chart.addLine(lineChart);*/


    }

    private void makeCharts(JsonEntity generateRoot) {


        JsonArray rootArray = generateRoot.getAsArray();
        for (int i = 0; i < 1; i++) {
            JsonObject obj = rootArray.get(i).getAsObject();
            JsonArray columns = obj.get("columns").getAsArray();
            JsonObject types = obj.get("types").getAsObject();
            JsonObject names = obj.get("names").getAsObject();
            JsonObject colors = obj.get("colors").getAsObject();

            long valuesX[] = new long[0];
            for (int j = 0; j < columns.getArray().size(); j++) {
                JsonArray p = columns.get(j).getAsArray();

                String type = p.get(0).getAsString();

                JsonEntity typeVal = types.get(type);


                String typeValString = typeVal.getAsString();
                switch (typeValString) {
                    case "line":
                        LineChart lineChart = new LineChart();

                        for (int k = 1; k < p.getArray().size(); k++) {
                            lineChart.addPoint(new ChartPoint(valuesX[k - 1], p.get(k).getAsFloat()));
                        }

                        lineChart.setName(names.get(type).getAsString());
                        lineChart.setColorLine(Color.parseColor(colors.get(type).getAsString()));
                        chart.addLine(lineChart);

                        break;
                    case "x":

                        valuesX = new long[p.size() - 1];

                        for (int k = 1; k < p.size(); k++) {

                            valuesX[k - 1] = p.get(k).getAsLong();
                        }

                        break;
                    default:
                        throw new JsonGExeption("Wrong type value \"" + typeValString + "\"");
                }
            }

        }


    }

    @Override
    public String getFormatX(float rawX) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d", Locale.US);
        return format.format(new Date((long) rawX));
    }

    @Override
    public String getFormatY(float rawY) {
        return String.valueOf((int)rawY);
    }
}
