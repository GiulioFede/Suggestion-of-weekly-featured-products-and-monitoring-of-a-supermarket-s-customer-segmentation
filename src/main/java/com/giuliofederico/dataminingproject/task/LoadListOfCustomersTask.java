package com.giuliofederico.dataminingproject.task;

import com.giuliofederico.dataminingproject.model.Customer;
import com.giuliofederico.dataminingproject.controller.HomePaneController;
import com.giuliofederico.dataminingproject.task.taskloader.LoadListOfCustomersTaskLoader;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Parent;

import java.util.ArrayList;

public class LoadListOfCustomersTask extends Service<Parent> {


    private HomePaneController homePaneController;
    private ArrayList<Customer> list;

    public LoadListOfCustomersTask(HomePaneController homePaneController, ArrayList<Customer> list) {
        this.homePaneController = homePaneController;
        this.list = list;
    }

    @Override
    protected Task<Parent> createTask() {
        return new LoadListOfCustomersTaskLoader(homePaneController,list);
    }

}