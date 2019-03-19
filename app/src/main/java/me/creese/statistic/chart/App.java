package me.creese.statistic.chart;

import android.app.Application;
import android.content.SharedPreferences;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences theme = getSharedPreferences("theme", MODE_PRIVATE);

        ThemeWrapper.get().init(theme.getInt("t",R.style.AppThemeDay),this);
    }
}
