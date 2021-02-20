package com.giuliofederico.dataminingproject.model;

import java.util.ArrayList;

public class IntegerAttributeInfo {

    ArrayList<Integer> numericalValuesList = new ArrayList<>();

    public ArrayList<Integer> getNumericalValuesList() {
        return numericalValuesList;
    }

    public void addValueInsideList(int value){
        numericalValuesList.add(value);
    }
}
