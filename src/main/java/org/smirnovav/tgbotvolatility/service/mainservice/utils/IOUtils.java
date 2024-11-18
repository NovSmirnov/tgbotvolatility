package org.smirnovav.tgbotvolatility.service.mainservice.utils;

import org.jfree.chart.JFreeChart;
import org.smirnovav.tgbotvolatility.service.mainservice.collectors.IntegratedFutVolLiq;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {




    private static void createDirectory(String directoryPath) {
        File file1 = new File(directoryPath);
        if (!file1.exists()) {
            boolean success = file1.mkdir();
            if (!success) {
                try {
                    throw new DirectoryNotEmptyException("Папка не может быть создана!");
                } catch (DirectoryNotEmptyException e) {
                    e.printStackTrace();
                }
            }
        }
        File file2 = new File(directoryPath + "\\temp");
        if (!file2.exists()) {
            boolean success = file2.mkdir();
            if (!success) {
                try {
                    throw new DirectoryNotEmptyException("Папка не может быть создана!");
                } catch (DirectoryNotEmptyException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<InputFile> getInputFiles(List<IntegratedFutVolLiq> integratedFutVolLiqList, String directoryPath) {
        createDirectory(directoryPath);
        List<JFreeChart> charts = ChartUtils.getCharts(integratedFutVolLiqList);
        List<InputFile> files = new ArrayList<>();
        File file = null;
        for (int i = 0; i < integratedFutVolLiqList.size(); i++) {
            file = new File(directoryPath + "\\temp" + "\\" + i + integratedFutVolLiqList.get(i).getSecId() + ".jpg");
            try {
                org.jfree.chart.ChartUtils.saveChartAsJPEG(file, charts.get(i), 800, 600);
                files.add(new InputFile().setMedia(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return files;
    }

    public static List<InputFile> getInputMultiLinesFiles(List<List<IntegratedFutVolLiq>> integratedFutVolLiqList, String directoryPath) {
        createDirectory(directoryPath);
        List<JFreeChart> charts = ChartUtils.getMultiLineCharts(integratedFutVolLiqList);
        List<InputFile> files = new ArrayList<>();
        File file = null;
        int counter = 1;
        for (int i = 0; i < integratedFutVolLiqList.size(); i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < integratedFutVolLiqList.get(i).size(); j++) {
                builder.append("_").append(counter).append(integratedFutVolLiqList.get(i).get(j).getSecId());
            }
            file = new File(directoryPath + "\\temp" + "\\" + builder + ".jpg");
            try {
                org.jfree.chart.ChartUtils.saveChartAsJPEG(file, charts.get(i), 800, 600);
                files.add(new InputFile().setMedia(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return files;
    }

    public static void cleanDirectory(String directoryPath) {
        File file = new File(directoryPath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File innerFile : files) {
                innerFile.delete();
            }
        }
    }


}
