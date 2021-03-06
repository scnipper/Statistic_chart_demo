package me.creese.statistic.chart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.io.DataInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import me.creese.statistic.chart.chart_view.Chart;
import me.creese.statistic.chart.chart_view.ChartPoint;
import me.creese.statistic.chart.chart_view.DrawThread;
import me.creese.statistic.chart.chart_view.LineChart;
import me.creese.statistic.chart.chart_view.impl.LineFormatter;
import me.creese.statistic.chart.jsonget.JsonArray;
import me.creese.statistic.chart.jsonget.JsonEntity;
import me.creese.statistic.chart.jsonget.JsonG;
import me.creese.statistic.chart.jsonget.JsonGExeption;
import me.creese.statistic.chart.jsonget.JsonObject;

public class MainActivity extends Activity implements LineFormatter, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Chart chart;
    private JsonEntity generateRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ThemeWrapper.get().getCurrTheme());
        setContentView(R.layout.activity_main);
        findViewById(R.id.container).setBackground(new ColorDrawable(ThemeWrapper.CONTAINER_COLOR));

        getActionBar().setTitle("Statistics");


        chart = findViewById(R.id.stat_chart);
        chart.setLineFormatter(this);

        try {
            DataInputStream stream = new DataInputStream(getResources().getAssets().open("chart_data.json"));

            byte bytes[] = new byte[stream.available()];
            stream.readFully(bytes);

            String fullJson = new String(bytes);

            JsonG jsonG = new JsonG();

            generateRoot = jsonG.generateRoot(fullJson);


        } catch (IOException e) {
            e.printStackTrace();
        }


        Spinner spinner = findViewById(R.id.spinner);


        Drawable spinnerDrawable = spinner.getBackground().getConstantState().newDrawable();

        spinnerDrawable.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        spinner.setBackground(spinnerDrawable);


        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_text);
        spinner.setAdapter(stringArrayAdapter);
        int sizeCharts = generateRoot.getAsArray().size();

        for (int i = 0; i < sizeCharts; i++) {
            stringArrayAdapter.add("Chart #" + (i + 1));
        }


    }

    /**
     * Make checkboxes for chart lines
     */
    @SuppressLint("RestrictedApi")
    private void addCheckBox() {
        ArrayList<LineChart> lines = chart.getLines();

        LinearLayout container = findViewById(R.id.check_container);
        for (int i = 0; i < lines.size(); i++) {
            LineChart line = lines.get(i);
            CheckBox checkBox = new CheckBox(new ContextThemeWrapper(this, R.style.CheckBoxStyle), null, 0);

            ColorStateList colorStateList = new ColorStateList(new int[][]{new int[]{-android.R.attr.state_checked}, new int[]{android.R.attr.state_checked}}, new int[]{line.getColor(), line.getColor()});
            checkBox.setButtonTintList(colorStateList);
            checkBox.setText(line.getName());
            checkBox.setTextColor(ThemeWrapper.TEXT_COLOR);
            container.addView(checkBox);

            checkBox.setTag(i);
            checkBox.setOnCheckedChangeListener(this);

            if (i < lines.size() - 1) {
                checkBox.setBackground(ThemeWrapper.backgroundCheckbox);
            }

        }
    }

    /**
     * Make chart from json root
     *
     * @param numChart num of chart
     */
    private void makeCharts(int numChart) {

        chart.clear();

        LinearLayout container = findViewById(R.id.check_container);

        container.removeAllViews();

        JsonArray rootArray = generateRoot.getAsArray();

        JsonObject obj = rootArray.get(numChart).getAsObject();
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

        chart.getDrawThread().requestRender();

        addCheckBox();


    }

    @Override
    public String getFormatX(float rawX) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d", Locale.US);
        return format.format(new Date((long) rawX));
    }

    @Override
    public String getFormatY(float rawY) {
        return String.valueOf((int) rawY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * Change theme when tap moon icon
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ThemeWrapper.get().switchTheme();
        recreate();

        return true;
    }

    /**
     * Check box line change clicked
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int numLine = (int) buttonView.getTag();

        ArrayList<LineChart> lines = chart.getLines();
        LineChart lineChart = lines.get(numLine);

        if (lineChart.isStartHideAnim() || lineChart.isStartShowAnim()) {
            buttonView.setChecked(!isChecked);
            return;
        }

        DrawThread drawThread = chart.getDrawThread();
        drawThread.clearTime();
        drawThread.requestRender();
        chart.hideViewLegend();

        if (!isChecked) {

            lineChart.hide();
        } else {

            lineChart.show();
        }
    }


    /**
     * Select chart in spinner
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        makeCharts(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
