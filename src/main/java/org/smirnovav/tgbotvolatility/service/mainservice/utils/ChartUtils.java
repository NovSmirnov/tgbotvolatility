package org.smirnovav.tgbotvolatility.service.mainservice.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.smirnovav.moex_lib.insideutils.DateTimeUtils;
import org.smirnovav.tgbotvolatility.service.mainservice.collectors.IntegratedFutVolLiq;
import org.smirnovav.tgbotvolatility.service.mainservice.volatility_service.VolatilityUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChartUtils {

    private ChartUtils() {

    }

    public static JFreeChart createLinearChart(double[] data, String lineName, String chartTitle,
                                               String xAxisName, PlotOrientation orientation,
                                               Color chartBackground, Color plotBackground, Color line,
                                               Color grid) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        final String series = lineName;
        for (int i = 0; i < data.length; i++) {
            String category = String.valueOf(i);
            dataset.addValue(data[i], series, category);
        }
        final JFreeChart chart = ChartFactory.createLineChart(
                chartTitle,
                null,
                xAxisName,
                dataset,
                orientation,
                true,
                true,
                false
        );
        chart.setBackgroundPaint(chartBackground);
        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(plotBackground);
        plot.setRangeGridlinePaint(grid);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);
        LineAndShapeRenderer renderer;
        renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(2.5f));

        Paint paint = line;
        renderer.setSeriesPaint(0, paint);
        plot.setRenderer(renderer);

        return chart;
    }

    public static JFreeChart createXYLinearChart(double[] data, String lineName, String chartTitle,
                                                 String xAxisName, PlotOrientation orientation,
                                                 Color chartBackground, Color plotBackground, Color line,
                                                 Color grid) {

        DefaultXYDataset dataset = new DefaultXYDataset();
        final String series = lineName;
        double[] firstArray = new double[data.length];
        double[] secondArray = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            firstArray[i] = i;
            secondArray[i] = data[i];
        }
        double[][] doubleData = {firstArray, secondArray};
        String seriesKey = "ATR, %";
        dataset.addSeries(seriesKey ,doubleData);

        final JFreeChart chart = ChartFactory.createXYLineChart(
                chartTitle,
                null,
                xAxisName,
                dataset,
                orientation,
                true,
                true,
                false
        );
        chart.setBackgroundPaint(chartBackground);

        final XYPlot plot = chart.getXYPlot();

        plot.setBackgroundPaint(plotBackground);
        plot.setRangeGridlinePaint(grid);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis(); // Получаем ось Y
//        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis(); // Получаем ось X
        ValueAxis domainAxis = plot.getDomainAxis();



//        domainAxis.setRange(10, 40);

        rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits()); // Устанавливаем цену деления на оси Y с десятыми долями
        rangeAxis.setAutoRangeIncludesZero(false); // Разрешаем не включать 0 в ось Y если график не включает нулевое значение

        XYSplineRenderer renderer = new XYSplineRenderer();
        renderer.setPrecision(10); // Устанавливаем сглаживание
        renderer.setSeriesShapesVisible(0, false);

        renderer.setSeriesStroke(0, new BasicStroke(3f));
        plot.setDataset(dataset);

        Paint paint = line;
        renderer.setSeriesPaint(0, paint);
        plot.setRenderer(renderer);
        return chart;
    }

    public static JFreeChart createXYMultiLinearChart(List<double[]> data, List<String> seriesKeys,
                                                      String chartTitle, String xAxisName, PlotOrientation orientation,
                                                      Color chartBackground, Color plotBackground, Color grid, Calendar lastDate) {
        if (isArraysSizesEquals(data)) {
            StringBuilder titleBuilder = new StringBuilder(chartTitle);
            titleBuilder.append(" ").append(seriesKeys.get(0));
            DefaultXYDataset dataset = new DefaultXYDataset();
            for (int i = 0; i < data.size(); i++) {
                double[] firstArray = new double[data.get(i).length];
                double[] secondArray = new double[data.get(i).length];
                for (int j = 0; j < data.get(i).length; j++) {
                    firstArray[j] = j;
                    secondArray[j] = data.get(i)[j];
                }
                double[][] doubleData = {firstArray, secondArray};
                dataset.addSeries(seriesKeys.get(i), doubleData);
                if (i != 0) {
                    titleBuilder.append(",").append(" ").append(seriesKeys.get(i));
                }
            }
            titleBuilder.append(" до ").append(DateTimeUtils.calendarToDateWithDash(lastDate)).append(".");
            final JFreeChart chart = ChartFactory.createXYLineChart(
                    titleBuilder.toString(),
                    null,
                    xAxisName,
                    dataset,
                    orientation,
                    true,
                    true,
                    false
            );
            chart.setBackgroundPaint(chartBackground);
            final XYPlot plot = chart.getXYPlot();
            plot.setBackgroundPaint(plotBackground);
            plot.setRangeGridlinePaint(grid);
            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis(); // Получаем ось Y
            rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits()); // Устанавливаем цену деления на оси Y с десятыми долями
            rangeAxis.setAutoRangeIncludesZero(false); // Разрешаем не включать 0 в ось Y если график не включает нулевое значение
            XYSplineRenderer renderer = new XYSplineRenderer();
            renderer.setPrecision(10); // Устанавливаем сглаживание
            plot.setDataset(dataset);
            List<Color> colors = colorGenerator(data.size());
            for (int i = 0; i < data.size(); i++) {
                renderer.setSeriesShapesVisible(i, false);
                renderer.setSeriesStroke(i, new BasicStroke(3f));
                renderer.setSeriesPaint(i, colors.get(i));
            }
            plot.setRenderer(renderer);
            return chart;
        } else {
            return null;
        }
    }

    public static List<JFreeChart> getCharts(List<IntegratedFutVolLiq> integratedFutVolLiqList) {
        List<JFreeChart> charts = new ArrayList<>();
        for (IntegratedFutVolLiq integratedFutVolLiq : integratedFutVolLiqList) {
            String chartTitle = "Динамика ATR % для " + integratedFutVolLiq.getShortName();
            JFreeChart chart = ChartUtils.createLinearChart(integratedFutVolLiq.getValues(), "ATR, %.",
                    chartTitle, "%", PlotOrientation.VERTICAL, Color.WHITE, Color.lightGray, Color.blue,
                    Color.BLACK);
            charts.add(chart);
        }
        return charts;
    }

    public static List<JFreeChart> getMultiLineCharts(List<List<IntegratedFutVolLiq>> integratedFutVolLiqList) {
        List<JFreeChart> charts = new ArrayList<>();
        for (List<IntegratedFutVolLiq> list : integratedFutVolLiqList) {
            List<double[]> data = new ArrayList<>();
            List<String> serialKeys = new ArrayList<>();
            for (IntegratedFutVolLiq integratedFutVolLiq : list) {
                data.add(integratedFutVolLiq.getValues());
                serialKeys.add(integratedFutVolLiq.getShortName());
            }
            JFreeChart chart = createXYMultiLinearChart(data, serialKeys, "Динамика ATR % для ",
                    "%", PlotOrientation.VERTICAL, Color.WHITE, Color.WHITE,
                    Color.BLACK, list.get(0).getFinishDate());
            charts.add(chart);
        }
        return charts;
    }

    public static void writeCharts(List<IntegratedFutVolLiq> integratedFutVolLiqList, String directoryPath) {
        List<JFreeChart> charts = getCharts(integratedFutVolLiqList);
        File file = null;
        for (int i = 0; i < integratedFutVolLiqList.size(); i++) {
            file = new File(directoryPath + "\\" + i + integratedFutVolLiqList.get(i).getSecId() + ".jpg");
            try {
                org.jfree.chart.ChartUtils.saveChartAsJPEG(file, charts.get(i), 800, 600);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeMultiLineCharts(List<IntegratedFutVolLiq> integratedFutVolLiqList, int numberOfLines, String directoryPath) {
        List<List<IntegratedFutVolLiq>> subLists = VolatilityUtils.createSubLists(integratedFutVolLiqList, numberOfLines);
        assert subLists != null;
        List<JFreeChart> charts = getMultiLineCharts(subLists);
        for (int i = 0; i < charts.size(); i++) {
            File file = null;
            StringBuilder fileNameBuilder = new StringBuilder(String.valueOf(i));
            for (IntegratedFutVolLiq integratedFutVolLiq : subLists.get(i)) {
                fileNameBuilder.append("-").append(integratedFutVolLiq.getSecId());
            }
            file = new File(directoryPath + "\\" + fileNameBuilder + ".jpg");
            try {
                org.jfree.chart.ChartUtils.saveChartAsJPEG(file, charts.get(i), 1000, 750);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isArraysSizesEquals(List<double[]> dataArrays) {
        int currentArraySize = dataArrays.get(0).length;
        for (double[] array : dataArrays) {
            if (array.length != currentArraySize) {
                return false;
            }
        }
        return true;
    }

    private static List<Color> colorGenerator(int numberOfColors) {
        List<Color> colors = new ArrayList<>();
        for (int i = 0; i < numberOfColors; i++) {
            int red = (int) (Math.random() * 255);
            if (red < 30) {
                red+=30;
            } else if (red > 225) {
                red-=30;
            }
            int green = (int) (Math.random() * 255);
            if (green < 30) {
                green+=30;
            } else if (red > 225) {
                green-=30;
            }
            int blue = (int) (Math.random() * 255);
            if (blue < 30) {
                blue+=30;
            } else if (red > 225) {
                blue-=30;
            }
            colors.add(new Color(red, green, blue));
        }
        return colors;
    }


}
