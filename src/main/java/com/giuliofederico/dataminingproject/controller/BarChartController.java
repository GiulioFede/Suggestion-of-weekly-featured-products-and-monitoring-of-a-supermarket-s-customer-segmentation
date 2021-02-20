package com.giuliofederico.dataminingproject.controller;

import com.giuliofederico.dataminingproject.model.CategoricalAttributeInfo;
import com.giuliofederico.dataminingproject.model.DoubleAttributeInfo;
import com.giuliofederico.dataminingproject.model.IntegerAttributeInfo;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class BarChartController {

    @FXML
    BarChart barChart;
    @FXML
    Text titleWindow;

    private String title;

    public void setTitle(String title){

        this.title = title;
        //set title of window
        titleWindow.setText(title);

    }

    public void loadBarChartForCategoricalInformation(ArrayList<CategoricalAttributeInfo> infoForEachCluster){

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("clusters");
        yAxis.setLabel("number of customers");

        //create labels
        for(int nLabel = 0; nLabel<infoForEachCluster.get(0).getLabels().size(); nLabel++) {
            XYChart.Series series = new XYChart.Series();
            series.setName(infoForEachCluster.get(0).getLabels().get(nLabel));

            //for each cluster show number of instances of a particolar label
            //for each cluster
            for(int cluster=0; cluster<infoForEachCluster.size(); cluster++) {
                series.getData().add(new XYChart.Data( "cluster "+(cluster+1),infoForEachCluster.get(cluster).getInstancesForLabels().get(nLabel)));
            }

            barChart.getData().addAll(series);
        }
    }

    public void loadBarChartForIntegerInformation(String titleSerie, ArrayList<IntegerAttributeInfo> infoForEachCluster){

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("clusters");
        yAxis.setLabel("number of customers");

        XYChart.Series series = new XYChart.Series();
        series.setName(titleSerie);

        //for each cluster show the average of numerical attribute
        //for each cluster
        for(int cluster=0; cluster<infoForEachCluster.size(); cluster++) {
            series.getData().add(new XYChart.Data( "cluster "+(cluster+1),infoForEachCluster.get(cluster).getNumericalValuesList().stream()
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0.0)));
        }

        barChart.getData().addAll(series);

    }

    public void loadBarChartForDoubleInformation(String titleSerie, ArrayList<DoubleAttributeInfo> infoForEachCluster){

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("clusters");
        yAxis.setLabel("number of customers");

        XYChart.Series series = new XYChart.Series();
        series.setName(titleSerie);

        //for each cluster show the average of numerical attribute
        //for each cluster
        for(int cluster=0; cluster<infoForEachCluster.size(); cluster++) {
            series.getData().add(new XYChart.Data( "cluster "+(cluster+1),infoForEachCluster.get(cluster).getNumericalValuesList().stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0)));
        }

        barChart.getData().addAll(series);

    }
}
