package com.giuliofederico.dataminingproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

public class PieChartController {

    @FXML
    PieChart pieChart;
    @FXML
    Text percentageReliabilityClusteringLabel;

    public void setTitle(String title){

        pieChart.setTitle(title);
    }

    public void setPercentageReliabilityClusteringLabel(String p){

        percentageReliabilityClusteringLabel.setText(p+"%");
    }

    public void loadPieChart(int[] numberOfInstancesOfClusters) {

        //set data inside g
        List<PieChart.Data> listData = new ArrayList<>();
        for(int i=0; i<numberOfInstancesOfClusters.length; i++){
            listData.add(new PieChart.Data("cluster "+(i+1),numberOfInstancesOfClusters[i]));
        }
        ObservableList<PieChart.Data> cakeGraphdata = FXCollections.observableArrayList(listData);

        pieChart.setData(cakeGraphdata);
    }
}
