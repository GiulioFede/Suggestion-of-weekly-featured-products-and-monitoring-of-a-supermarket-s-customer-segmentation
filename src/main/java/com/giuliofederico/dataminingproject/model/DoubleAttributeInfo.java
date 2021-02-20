package com.giuliofederico.dataminingproject.model;

import java.util.ArrayList;

public class DoubleAttributeInfo {

    ArrayList<Double> numericalValuesList = new ArrayList<>();

    public ArrayList<Double> getNumericalValuesList() {
        return numericalValuesList;
    }

    public void addValueInsideList(double value){
        numericalValuesList.add(value);
    }
}