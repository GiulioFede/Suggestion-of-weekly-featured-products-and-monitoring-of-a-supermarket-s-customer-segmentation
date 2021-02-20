package com.giuliofederico.dataminingproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;


public class SuggestionController {

    @FXML
    private Label ruleLabel;
    @FXML
    private Text explicationLabel;

    public void setRule(String rule){

        ruleLabel.setText(rule);
    }

    public void setExplication(String explication){

        explicationLabel.setText(explication);
    }
}
