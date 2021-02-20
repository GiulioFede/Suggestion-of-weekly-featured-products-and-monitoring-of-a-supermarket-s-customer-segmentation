package com.giuliofederico.dataminingproject.task;

import com.giuliofederico.dataminingproject.controller.HomePaneController;
import com.giuliofederico.dataminingproject.task.taskloader.ClusteringTaskLoader;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Parent;

public class ClusteringTask extends Service<Parent> {


    private HomePaneController homePaneController;

    public ClusteringTask(HomePaneController homePaneController) {
        this.homePaneController = homePaneController;
    }

    @Override
    protected Task<Parent> createTask() {
        return new ClusteringTaskLoader(homePaneController);
    }
}