package me.creese.statistic.chart;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;


public class ThemeWrapper {

    private static ThemeWrapper instance;
    private int currTheme;

    public static int BOTTOM_SQUARES_COLOR;
    public static int CHART_TEXT_COLOR;
    public static int CONTAINER_COLOR;
    public static int CHART_LINE_COLOR;
    public static int SIZE_RECT_COLOR;
    public static int VIEW_LEGEND_BACKGROUND_COLOR;
    public static int TEXT_COLOR;
    public static Drawable backgroundCheckbox;
    private Context context;

    public static ThemeWrapper get() {
        if(instance == null) instance = new ThemeWrapper();
        return instance;
    }

    public void init(int currTheme, Context context) {
        this.currTheme = currTheme;
        this.context = context;

        if(currTheme == R.style.AppThemeDay) {
            CONTAINER_COLOR = Color.WHITE;
            CHART_LINE_COLOR = context.getResources().getColor(R.color.chartLineColor);
            CHART_TEXT_COLOR = 0xff96a2aa;
            BOTTOM_SQUARES_COLOR = 0x88517da2;
            SIZE_RECT_COLOR = 0xbbdbe7f0;
            VIEW_LEGEND_BACKGROUND_COLOR = Color.WHITE;
            TEXT_COLOR = Color.BLACK;
            backgroundCheckbox = context.getResources().getDrawable(R.drawable.check_box_background);

        } else {
            CONTAINER_COLOR = context.getResources().getColor(R.color.containerColorNight);
            CHART_LINE_COLOR = context.getResources().getColor(R.color.chartLineColorNight);
            CHART_TEXT_COLOR = 0xff506372;
            BOTTOM_SQUARES_COLOR = 0xaa19232e;
            SIZE_RECT_COLOR = 0xbb2b4256;
            VIEW_LEGEND_BACKGROUND_COLOR = context.getResources().getColor(R.color.colorPrimaryDarkTheme);
            TEXT_COLOR = Color.WHITE;
            backgroundCheckbox = context.getResources().getDrawable(R.drawable.check_box_background_night);

        }
    }

    public void switchTheme() {
        int initTheme = 0;
        if(currTheme == R.style.AppThemeDay) {
            initTheme = R.style.AppThemeNight;
        } else initTheme = R.style.AppThemeDay;
        SharedPreferences pref = context.getSharedPreferences(App.PERF_THEME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("t",initTheme);
        editor.apply();

        init(initTheme,context);
    }

    public int getCurrTheme() {
        return currTheme;
    }
}
