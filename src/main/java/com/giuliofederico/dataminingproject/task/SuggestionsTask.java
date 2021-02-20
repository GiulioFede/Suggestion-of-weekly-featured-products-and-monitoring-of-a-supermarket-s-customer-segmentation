package com.giuliofederico.dataminingproject.task;

import com.giuliofederico.dataminingproject.controller.HomePaneController;
import com.giuliofederico.dataminingproject.task.taskloader.SuggestionsTaskLoader;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Parent;

public class SuggestionsTask extends Service<Parent> {


        private HomePaneController homePaneController;
        private int numberOfSuggestions;
        private double confidence;

        public SuggestionsTask(HomePaneController homePaneController, int numberOfSuggestions, double confidence) {
            this.homePaneController = homePaneController;
            this.numberOfSuggestions = numberOfSuggestions;
            this.confidence = confidence;
        }

        @Override
        protected Task<Parent> createTask() {
            return new SuggestionsTaskLoader(homePaneController,numberOfSuggestions,confidence);
        }
}
