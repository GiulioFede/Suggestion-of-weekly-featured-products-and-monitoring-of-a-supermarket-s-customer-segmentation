package com.giuliofederico.dataminingproject.model;

public class Customer {

    String name, age, cluster;

    public Customer(String name, String age, String cluster) {
        this.name = name;
        this.age = age;
        this.cluster = cluster;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }
}
