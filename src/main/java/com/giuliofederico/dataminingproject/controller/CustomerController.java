package com.giuliofederico.dataminingproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class CustomerController {

    @FXML
    Label nameLabel, ageLabel, clusterLabel;

    public void setNameLabel(String nameLabel) {
        this.nameLabel.setText(nameLabel);
    }

    public void setAgeLabel(String ageLabel) {
        this.ageLabel.setText(ageLabel);
    }

    public void setClusterLabel(String clusterLabel) {
        this.clusterLabel.setText(clusterLabel);
    }
}
