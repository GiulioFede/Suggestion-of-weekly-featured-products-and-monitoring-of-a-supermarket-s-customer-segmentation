package com.giuliofederico.dataminingproject.task.taskloader;

import com.giuliofederico.dataminingproject.model.Customer;
import com.giuliofederico.dataminingproject.controller.CustomerController;
import com.giuliofederico.dataminingproject.controller.HomePaneController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class LoadListOfCustomersTaskLoader extends Task<Parent> {

    private HomePaneController homePaneController;
    private ArrayList<Customer> customers;

    public LoadListOfCustomersTaskLoader(HomePaneController homePaneController, ArrayList<Customer> customers) {
        this.homePaneController = homePaneController;
        this.customers = customers;
    }


    @Override
    protected Parent call() throws Exception {

        Collections.shuffle(customers);

        for(Customer customer: customers) {

            //add new barChart UI
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "../../fxml/customerModel.fxml"));
            try {
                final Parent customerPane = (Parent) loader.load();
                CustomerController customerController = loader.getController();
                //customize
                customerController.setNameLabel(customer.getName());
                customerController.setAgeLabel(customer.getAge());
                customerController.setClusterLabel(customer.getCluster());

                Platform.runLater(() -> {
                    homePaneController.addToBoxDetails(customerPane);
                });

                Thread.sleep(1000); //IMPORTANT: relax cpu

            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    homePaneController.setProgressIndicatorClusteringAnalysisVisibility(false);
                });
            }
        }


        return null;
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        Platform.runLater(() -> {
            homePaneController.setProgressIndicatorClusteringAnalysisVisibility(false);
        });
    }

    @Override
    protected void failed() {
        super.failed();
        Platform.runLater(() -> {
            homePaneController.setProgressIndicatorClusteringAnalysisVisibility(false);
        });
    }
}