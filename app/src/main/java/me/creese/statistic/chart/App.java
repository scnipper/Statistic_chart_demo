package me.creese.statistic.chart;

import android.app.Application;
import android.content.SharedPreferences;

public class App extends Application {
    public static final String PERF_THEME = "theme";

    @Override
    public void onCreate() {
        super.onCreate();

        // get theme form preferences
        SharedPreferences theme = getSharedPreferences(PERF_THEME, MODE_PRIVATE);

        ThemeWrapper.get().init(theme.getInt("t",R.style.AppThemeDay),this);
    }
}
