package me.creese.statistic.chart.chart_view.impl;


public interface LineFormatter {

    /**
     * Format raw value X
     * @param rawX
     * @return
     */
    String getFormatX(float rawX);

    /**
     * Format raw value Y
     * @param rawY
     * @return
     */
    String getFormatY(float rawY);
}
