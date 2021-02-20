package com.giuliofederico.dataminingproject.controller;

import com.giuliofederico.dataminingproject.model.CategoricalAttributeInfo;
import com.giuliofederico.dataminingproject.model.Customer;
import com.giuliofederico.dataminingproject.model.DoubleAttributeInfo;
import com.giuliofederico.dataminingproject.model.IntegerAttributeInfo;
import com.giuliofederico.dataminingproject.task.ClusteringTask;
import com.giuliofederico.dataminingproject.task.LoadListOfCustomersTask;
import com.giuliofederico.dataminingproject.task.SuggestionsTask;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import weka.associations.AssociationRule;
import weka.associations.Item;
import weka.clusterers.AbstractClusterer;
import weka.core.Instance;
import weka.core.Instances;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HomePaneController implements Initializable {

    //::::::::::::::::::::::::::::::::::::::::properties of basket analysis:::::::::::::::::::::::::::::::::::::::::::::
    @FXML
    Pane basketAnalysisPane;
    @FXML
    ProgressIndicator progressIndicatorBasketAnalysis;
    @FXML
    VBox boxSuggestions;
    @FXML
    TextField textFieldNumberOfSuggestions, textFieldConfidence;
    //__________________________________________________________________________________________________________________
    //::::::::::::::::::::::::::::::::::::::::properties of clustering analysis:::::::::::::::::::::::::::::::::::::::::
    @FXML
    Pane clusteringAnalysisPane;
    @FXML
    VBox boxDetails;
    @FXML
    ProgressIndicator progressIndicatorClusteringAnalysis;
    @FXML
    Button startClusteringButton;
    @FXML
    ImageView scrollImage;
    @FXML
    Text scrollTextLabel;

    //__________________________________________________________________________________________________________________

//:::::::::::::::::::::::::::::::::::::::::::: BASKET ANALYSIS::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void openBasketAnalysisPane(MouseEvent mouseEvent) {

        System.out.println("open basket analysis pane");

        //init
        initBasketAnalysisPane();
    }

    private void initBasketAnalysisPane() {
        basketAnalysisPane.setVisible(true);

        //clear actual displayed rules
        boxSuggestions.getChildren().clear();

        //clear text field
        textFieldNumberOfSuggestions.setText("");
        textFieldConfidence.setText("");
    }

    public void loadWeeklySuggestions(MouseEvent mouseEvent) {

        //make visible progress indicator
        setProgressIndicatorBasketAnalysisVisibility(true);

        //clear actual displayed rules
        boxSuggestions.getChildren().clear();

        //get parameter of mining
        int numberOfSuggestions;
        double confidence;
        try {
            numberOfSuggestions = Integer.valueOf(textFieldNumberOfSuggestions.getText());
        }catch (Exception e) {
            numberOfSuggestions = 10; //default
        }

        if(numberOfSuggestions>100)
            numberOfSuggestions = 100;

        try {
            confidence = Double.valueOf(textFieldConfidence.getText());
            if(confidence>1 || confidence<0)
                throw new Exception();
        }catch (Exception e) {
            confidence = 0.4; //default
        }


        //start weekly suggestions task
        SuggestionsTask suggestionsTask = new SuggestionsTask(this,numberOfSuggestions,confidence);
        suggestionsTask.start();

    }

    public void addNewSuggestion(AssociationRule associationRule){

        try {
            Collection<Item> premise = associationRule.getPremise();
            Collection<Item> consequence = associationRule.getConsequence();
            double confidence = associationRule.getPrimaryMetricValue();

            ArrayList<String> itemsOfPremise = new ArrayList<>();
            String itemOfConsequence = "";
            Iterator iterator = premise.iterator();
            Iterator iterator2 = consequence.iterator();

            //get the item of association
            while (iterator.hasNext())
                itemsOfPremise.add(((Item)iterator.next()).getAttribute().name());
            while (iterator2.hasNext())
                itemOfConsequence = ((Item)iterator2.next()).getAttribute().name();


            //add new suggestion UI
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "../fxml/suggestionModel.fxml"));
            final Parent suggestionPane = (Parent) loader.load();

            SuggestionController suggestionController = (SuggestionController) loader.getController();
            suggestionController.setRule(itemsOfPremise.toString()+" -> "+itemOfConsequence);
            suggestionController.setExplication("Buying " + itemsOfPremise + " implies a " + String.format("%.2f",100 * confidence)+ "% probability to buy also " + itemOfConsequence);

            boxSuggestions.getChildren().add(suggestionPane);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setProgressIndicatorBasketAnalysisVisibility(boolean visibility){

        progressIndicatorBasketAnalysis.setVisible(visibility);
    }

    public void closeBasketAnalysisPane(MouseEvent mouseEvent) {

        basketAnalysisPane.setVisible(false);
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::


    //:::::::::::::::::::::::::::::::::::::::::CLUSTER ANALYSIS:::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void openClusteringAnalysisPane(MouseEvent mouseEvent) {

        System.out.println("open clustering analysis pane");

        //init
        initClusteringAnalysisPane();
    }

    private void initClusteringAnalysisPane() {
        clusteringAnalysisPane.setVisible(true);
        setProgressIndicatorClusteringAnalysisVisibility(false);
        setStartClusteringButtonVisibility(true);
        scrollImage.setVisible(false);
        scrollTextLabel.setVisible(false);
    }

    public void setProgressIndicatorClusteringAnalysisVisibility(boolean visibility){

        progressIndicatorClusteringAnalysis.setVisible(visibility);
    }

    public void setStartClusteringButtonVisibility(boolean visibility){

        if(visibility==true){
            startClusteringButton.setOpacity(1);
            startClusteringButton.setOnMouseClicked(this::startClustering);
        }else {
            startClusteringButton.setOpacity(0.4);
            startClusteringButton.setOnMouseClicked(null);
        }
    }

    public void closeClusteringAnalysisPane(MouseEvent mouseEvent) {

        clusteringAnalysisPane.setVisible(false);
    }

    public void loadPieChart(int[] numberOfInstancesOfClusters, double quality){

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "../fxml/pieChartModel.fxml"));
        try {
            final Parent pieChart= (Parent) loader.load();
            PieChartController pieChartController = loader.getController();
            //add to ui
            addToBoxDetails(pieChart);
            //customize
            pieChartController.setTitle("Distribution of customers among clusters");
            pieChartController.setPercentageReliabilityClusteringLabel(String.format("%2.1f",quality*100));
            pieChartController.loadPieChart(numberOfInstancesOfClusters);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadCategoricalBarChart(String title, ArrayList<CategoricalAttributeInfo> infoClusters){

        //add new barChart UI
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "../fxml/barChartModel.fxml"));
        try {
            final Parent barChartPane= (Parent) loader.load();
            BarChartController barChartController = loader.getController();
            //add to ui
            addToBoxDetails(barChartPane);
            //customize
            barChartController.setTitle(title);
            barChartController.loadBarChartForCategoricalInformation(infoClusters);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadNumericalBarChart(String title,String titleSerie, ArrayList<IntegerAttributeInfo> infoClusters){

        //add new barChart UI
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "../fxml/barChartModel.fxml"));
        try {
            final Parent barChartPane= (Parent) loader.load();
            BarChartController barChartController = loader.getController();
            //add to ui
            addToBoxDetails(barChartPane);
            //customize
            barChartController.setTitle(title);
            barChartController.loadBarChartForIntegerInformation(titleSerie,infoClusters);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDoubleBarChart(String title,String titleSerie, ArrayList<DoubleAttributeInfo> infoClusters){

        //add new barChart UI
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "../fxml/barChartModel.fxml"));
        try {
            final Parent barChartPane= (Parent) loader.load();
            BarChartController barChartController = loader.getController();
            //add to ui
            addToBoxDetails(barChartPane);
            //customize
            barChartController.setTitle(title);
            barChartController.loadBarChartForDoubleInformation(titleSerie,infoClusters);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToBoxDetails(Parent barChartPane) {

        boxDetails.getChildren().add(barChartPane);
    }

    public void startClustering(MouseEvent mouseEvent) {

        //make visible progress indicator
        setProgressIndicatorClusteringAnalysisVisibility(true);
        //hide button
        setStartClusteringButtonVisibility(false);
        //reset content
        clearBoxDetails();

        ClusteringTask clusteringTask = new ClusteringTask(this);
        clusteringTask.start();

    }

    //called by second thread when clustering ends
    public void processClusteringResult(Instances dataset, Instances datasetDenormalized, AbstractClusterer clustering, double quality) {


        try {
           //initialization______________________________________________________________________________________________________________

           int[] numberOfInstancesOfClusters = new int[clustering.numberOfClusters()];

           ArrayList<CategoricalAttributeInfo> marriedCustomersInfoForEachCluster = new ArrayList<>();
           for(int i=0; i<clustering.numberOfClusters(); i++) {
               marriedCustomersInfoForEachCluster.add(new CategoricalAttributeInfo());
               marriedCustomersInfoForEachCluster.get(i).setLabels(new ArrayList<String>() {{
                   add("married");
                   add("unmarried");
               }});
               marriedCustomersInfoForEachCluster.get(i).setInstancesForLabels(new ArrayList<Integer>() {{
                   add(0);
                   add(0);
               }});
           }

           ArrayList<CategoricalAttributeInfo> sexCustomersInfoForEachCluster = new ArrayList<>();
           for(int i=0; i<clustering.numberOfClusters(); i++) {
               sexCustomersInfoForEachCluster.add(new CategoricalAttributeInfo());
               sexCustomersInfoForEachCluster.get(i).setLabels(new ArrayList<String>() {{
                   add("male");
                   add("female");
               }});
               sexCustomersInfoForEachCluster.get(i).setInstancesForLabels(new ArrayList<Integer>() {{
                   add(0);
                   add(0);
               }});
           }

           ArrayList<IntegerAttributeInfo> ageCustomersInfoForEachCluster = new ArrayList<>();
           for(int i=0; i<clustering.numberOfClusters(); i++) {
               ageCustomersInfoForEachCluster.add(new IntegerAttributeInfo());
           }

           ArrayList<CategoricalAttributeInfo> jobCategoryCustomersInfoForEachCluster = new ArrayList<>();
           for(int i=0; i<clustering.numberOfClusters(); i++) {
               jobCategoryCustomersInfoForEachCluster.add(new CategoricalAttributeInfo());
               jobCategoryCustomersInfoForEachCluster.get(i).setLabels(new ArrayList<String>() {{
                   add("Retail");
                   add("n/a"); //TODO: eliminarlo dopo il data preprocessing
                   add("Property");
                   add("Manufacturing");
                   add("Health");
                   add("Financial Services");
                   add("IT");
                   add("Entertainment");
                   add("Agriculture");
                   add("Telecommunications");
               }});
               jobCategoryCustomersInfoForEachCluster.get(i).setInstancesForLabels(new ArrayList<Integer>() {{
                   add(0);
                   add(0);
                   add(0);
                   add(0);
                   add(0);
                   add(0);
                   add(0);
                   add(0);
                   add(0);
                   add(0);
               }});
           }

           ArrayList<CategoricalAttributeInfo> statusCustomersInfoForEachCluster = new ArrayList<>();
           for(int i=0; i<clustering.numberOfClusters(); i++) {
               statusCustomersInfoForEachCluster.add(new CategoricalAttributeInfo());
               statusCustomersInfoForEachCluster.get(i).setLabels(new ArrayList<String>() {{
                   add("Affluent Customer");
                   add("Mass Customer");
                   add("High Net Worth");
               }});
               statusCustomersInfoForEachCluster.get(i).setInstancesForLabels(new ArrayList<Integer>() {{
                   add(0);
                   add(0);
                   add(0);
               }});
           }

           ArrayList<IntegerAttributeInfo> annualExpensesCustomersInfoForEachCluster = new ArrayList<>();
           for(int i=0; i<clustering.numberOfClusters(); i++) {
               annualExpensesCustomersInfoForEachCluster.add(new IntegerAttributeInfo());
           }

           ArrayList<DoubleAttributeInfo> beverageCustomersInfoForEachCluster = new ArrayList<>();
           for(int i=0; i<clustering.numberOfClusters(); i++) {
               beverageCustomersInfoForEachCluster.add(new DoubleAttributeInfo());
           }

           ArrayList<DoubleAttributeInfo> bakeryCustomersInfoForEachCluster = new ArrayList<>();
           for(int i=0; i<clustering.numberOfClusters(); i++) {
               bakeryCustomersInfoForEachCluster.add(new DoubleAttributeInfo());
           }

           ArrayList<DoubleAttributeInfo> cannedFoodsCustomersInfoForEachCluster = new ArrayList<>();
           for(int i=0; i<clustering.numberOfClusters(); i++) {
               cannedFoodsCustomersInfoForEachCluster.add(new DoubleAttributeInfo());
           }

           ArrayList<DoubleAttributeInfo> dryFoodsCustomersInfoForEachCluster = new ArrayList<>();
           for(int i=0; i<clustering.numberOfClusters(); i++) {
               dryFoodsCustomersInfoForEachCluster.add(new DoubleAttributeInfo());
           }

           ArrayList<DoubleAttributeInfo> frozenFoodsCustomersInfoForEachCluster = new ArrayList<>();
           for(int i=0; i<clustering.numberOfClusters(); i++) {
               frozenFoodsCustomersInfoForEachCluster.add(new DoubleAttributeInfo());
           }

           ArrayList<DoubleAttributeInfo> meatCustomersInfoForEachCluster = new ArrayList<>();
           for(int i=0; i<clustering.numberOfClusters(); i++) {
               meatCustomersInfoForEachCluster.add(new DoubleAttributeInfo());
           }

           ArrayList<DoubleAttributeInfo> produceCustomersInfoForEachCluster = new ArrayList<>();
           for(int i=0; i<clustering.numberOfClusters(); i++) {
               produceCustomersInfoForEachCluster.add(new DoubleAttributeInfo());
           }

           ArrayList<DoubleAttributeInfo> cleanersCustomersInfoForEachCluster = new ArrayList<>();
           for(int i=0; i<clustering.numberOfClusters(); i++) {
               cleanersCustomersInfoForEachCluster.add(new DoubleAttributeInfo());
           }

           ArrayList<DoubleAttributeInfo> othersCustomersInfoForEachCluster = new ArrayList<>();
           for(int i=0; i<clustering.numberOfClusters(); i++) {
               othersCustomersInfoForEachCluster.add(new DoubleAttributeInfo());
           }

           ArrayList<Customer> customersInfo = new ArrayList<>();

           //computing_____________________________________________________________________________________________________________________

           for (int i = 0; i < dataset.numInstances(); i++) { //TODO: quando si elimina l'attributo Income bisogna anche aggiustare gli indici usati sotto

               Instance instance = dataset.instance(i);
               Instance instanceDenormalized = datasetDenormalized.instance(i);


               int membershipCluster = clustering.clusterInstance(instance);

               numberOfInstancesOfClusters[membershipCluster]++;

               if(instance.stringValue(0).compareTo("married")==0)
                   marriedCustomersInfoForEachCluster.get(membershipCluster).incrementNumberOfInstancesOfLabels(0);
               else
                   marriedCustomersInfoForEachCluster.get(membershipCluster).incrementNumberOfInstancesOfLabels(1);

               if(instance.stringValue(1).compareTo("Male")==0)
                   sexCustomersInfoForEachCluster.get(membershipCluster).incrementNumberOfInstancesOfLabels(0);
               else
                   sexCustomersInfoForEachCluster.get(membershipCluster).incrementNumberOfInstancesOfLabels(1);

               ageCustomersInfoForEachCluster.get(membershipCluster).addValueInsideList((int)instanceDenormalized.value(6));

               jobCategoryCustomersInfoForEachCluster.get(membershipCluster).incrementNumberOfInstancesOfLabels(instanceDenormalized.stringValue(8));

               statusCustomersInfoForEachCluster.get(membershipCluster).incrementNumberOfInstancesOfLabels(instanceDenormalized.stringValue(9));

               annualExpensesCustomersInfoForEachCluster.get(membershipCluster).addValueInsideList((int)instanceDenormalized.value(11));

               beverageCustomersInfoForEachCluster.get(membershipCluster).addValueInsideList(instanceDenormalized.value(12));

               bakeryCustomersInfoForEachCluster.get(membershipCluster).addValueInsideList(instanceDenormalized.value(13));

               cannedFoodsCustomersInfoForEachCluster.get(membershipCluster).addValueInsideList(instanceDenormalized.value(14));

               dryFoodsCustomersInfoForEachCluster.get(membershipCluster).addValueInsideList(instanceDenormalized.value(15));

               frozenFoodsCustomersInfoForEachCluster.get(membershipCluster).addValueInsideList(instanceDenormalized.value(16));

               meatCustomersInfoForEachCluster.get(membershipCluster).addValueInsideList(instanceDenormalized.value(17));

               produceCustomersInfoForEachCluster.get(membershipCluster).addValueInsideList(instanceDenormalized.value(18));

               cleanersCustomersInfoForEachCluster.get(membershipCluster).addValueInsideList(instanceDenormalized.value(19));

               othersCustomersInfoForEachCluster.get(membershipCluster).addValueInsideList(instanceDenormalized.value(20));

               customersInfo.add(new Customer(instanceDenormalized.stringValue(1),String.format("%.0f",instanceDenormalized.value(6)),"cluster "+membershipCluster));
           }

           //1°: load summary
           loadPieChart(numberOfInstancesOfClusters, quality);

           scrollTextLabel.setVisible(true);
           scrollImage.setVisible(true);

           //2°: load chart bar of Married/Unmarried
           loadCategoricalBarChart("Number of married/unmarried customers for each cluster",marriedCustomersInfoForEachCluster);

           //3°: load chart bar of sex male/female
           loadCategoricalBarChart("Number of male/female customers for each cluster",sexCustomersInfoForEachCluster);

           //4°: load chart bar of average age
           loadNumericalBarChart("Average of age for each cluster","average age",ageCustomersInfoForEachCluster);

           //5°: load chart bar of jobCategory
           loadCategoricalBarChart("Number of job's categories for customers of each cluster",jobCategoryCustomersInfoForEachCluster);

           //6°: load chart bar of status
           loadCategoricalBarChart("Customer status for each cluster",statusCustomersInfoForEachCluster);

           //7°: load chart bar of average of annual expenses
           loadNumericalBarChart("Average of annual expenses for each cluster","annual expenses",annualExpensesCustomersInfoForEachCluster);

           //8°: load chart bar of beverage
           loadDoubleBarChart("Average of beverage's popularity for each cluster","beverage popularity",beverageCustomersInfoForEachCluster);

           //9°: load chart bar of backery
           loadDoubleBarChart("Average of bakery's popularity for each cluster","bakery popularity",bakeryCustomersInfoForEachCluster);

           //10°: load chart bar of canned foods
           loadDoubleBarChart("Average of canned food's popularity for each cluster","canned foods popularity",cannedFoodsCustomersInfoForEachCluster);

           //11°: load chart bar of dry foods
           loadDoubleBarChart("Average of dry food's popularity for each cluster","dry foods popularity",dryFoodsCustomersInfoForEachCluster);

           //12°: load chart bar of frozen foods
           loadDoubleBarChart("Average of frozen food's popularity for each cluster","frozen foods popularity",frozenFoodsCustomersInfoForEachCluster);

           //13°: load chart bar of meat foods
           loadDoubleBarChart("Average of meat's popularity for each cluster","meat popularity",meatCustomersInfoForEachCluster);

           //14°: load chart bar of produce foods
           loadDoubleBarChart("Average of produce's popularity for each cluster","produce popularity",produceCustomersInfoForEachCluster);

           //15°: load chart bar of cleaners
           loadDoubleBarChart("Average of cleaner's popularity for each cluster","cleaners popularity",cleanersCustomersInfoForEachCluster);

           //16°: load chart bar of others
           loadDoubleBarChart("Average of other's popularity for each cluster","others popularity",othersCustomersInfoForEachCluster);

          //17°: load all customers
           //due to the fact that it's really heavy computationally load all the customers they will be loaded in background
           LoadListOfCustomersTask loadListOfCustomersTask = new LoadListOfCustomersTask(this, customersInfo);
           loadListOfCustomersTask.start();



       } catch (Exception e) {
           e.printStackTrace();
           setProgressIndicatorClusteringAnalysisVisibility(false);
           setStartClusteringButtonVisibility(true);
       }

    }

    public void clearBoxDetails() {

        boxDetails.getChildren().clear();
    }


    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void initialize(URL location, ResourceBundle resources) {
        //hide two main pane
        basketAnalysisPane.setVisible(false);
        setProgressIndicatorBasketAnalysisVisibility(false);
    }

}
