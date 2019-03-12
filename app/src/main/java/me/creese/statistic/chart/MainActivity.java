package me.creese.statistic.chart;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

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


    }
}
