package com.giuliofederico.dataminingproject.model;

import java.util.ArrayList;

//this class model is used to contain info of clustering result useful for passing them to bar chart
public class CategoricalAttributeInfo {

    ArrayList<String> labels;
    ArrayList<Integer> instancesForLabels;

    public CategoricalAttributeInfo(ArrayList<String> labels, ArrayList<Integer> instancesForLabels) {
        this.labels = labels;
        this.instancesForLabels = instancesForLabels;
    }

    public CategoricalAttributeInfo(){

    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }

    public ArrayList<Integer> getInstancesForLabels() {
        return instancesForLabels;
    }

    public void setInstancesForLabels(ArrayList<Integer> instancesForLabels) {
        this.instancesForLabels = instancesForLabels;
    }

    public void incrementNumberOfInstancesOfLabels(int label){
        instancesForLabels.set(label,instancesForLabels.get(label)+1);
    }

    public void incrementNumberOfInstancesOfLabels(String label){
        instancesForLabels.set(labels.indexOf(label),instancesForLabels.get(labels.indexOf(label))+1);
    }
}
