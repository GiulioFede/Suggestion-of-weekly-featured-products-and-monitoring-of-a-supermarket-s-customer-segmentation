package com.giuliofederico.dataminingproject.task.taskloader;

import com.giuliofederico.dataminingproject.controller.HomePaneController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import weka.associations.Apriori;
import weka.associations.AssociationRule;
import weka.associations.AssociationRules;
import weka.associations.FPGrowth;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class SuggestionsTaskLoader extends Task<Parent> {

    private HomePaneController homePaneController;
    private int numberOfSuggestions;
    private double confidence;
    static ArrayList<String> supermarketItemset = new ArrayList<>(Arrays.asList(("water,spaghetti,salt,sugar,eggs,flour,coffee,biscuits,butter,onions,bread,milk,apples,tomatoes,chicken,lettuce,oranges,peaches,pears,prunes,plums,strawberries,raisins,kiwi,broccoli,carrots,celery,green-peppers,corn,mushrooms,hamburger,fresh fish,pork chops,pork and beans,meat balls,jelly,cheese,potatoes,ketchup,yogurt,cereal,orange juice,prune juice,ice cream,soda,soup,chocolate,mayonnaise,mustard,rice,laundry soap,soap,tissue,toilet paper,paper towels,beer,cake,crackers,tea,oil,vinegar,pepper,cinnamon,ginger,vanilla,noodles,potato chips,diapers,sponges,baby formula,strained baby food,baby lotion,hand lotion,batteries,shopping bag").replace(" ","-").split(",")));


    public SuggestionsTaskLoader(HomePaneController homePaneController, int numberOfSuggestions, double confidence) {
        this.homePaneController = homePaneController;
        this.numberOfSuggestions = numberOfSuggestions;
        this.confidence = confidence;

    }


    @Override
    protected Parent call() throws Exception {


            //load basketDatabase
            ArrayList<ArrayList<String>> dataset = loadBasketDatabase();


            if (dataset.size() > 0) {

                //derive dataset for weka
                //for each item create new attribute
                ArrayList<Attribute> attributes = new ArrayList<Attribute>();

                for (int i = 0; i < supermarketItemset.size(); i++) {

                    ArrayList<String> labels = new ArrayList<>();
                    labels.add("0");
                    labels.add("1");
                    Attribute itemAttribute = new Attribute(supermarketItemset.get(i), labels);
                    attributes.add(itemAttribute);
                }


                Instances wekaBasketDatabase = new Instances("BasketDataset", attributes, 0);

                //create instance and add it to the dataset (convert it into double)
                for (ArrayList<String> transaction : dataset) {

                    //add cells for each one of the attributes
                    double[] attributesValues = new double[supermarketItemset.size()];
                    System.out.println(transaction);
                    for (int i = 0; i < wekaBasketDatabase.numAttributes(); i++) {
                        attributesValues[i] = wekaBasketDatabase.attribute(i).indexOfValue(transaction.get(i));
                    }

                    //add instance
                    Instance instance = new DenseInstance(1.0, attributesValues);
                    wekaBasketDatabase.add(instance);

                }


                //Here you can choose two possible algorithms to mine association rules: Apriori or FP-Growth.
                //Due to better performance of FP-Growth it will be chosen by default.

                findRulesUsingFPGrowth(wekaBasketDatabase);   //if you want FP-Growth
                //findRulesUsingApriori(wekaBasketDatabase);  //if you want Apriori

            }
            return null;
    }

    //use Apriori to find rules
    private void findRulesUsingApriori(Instances wekaBasketDatabase) {

        try {
            System.out.println("start Apriori");
            //start Apriori...
            Apriori apriori = new Apriori();
            String[] aprioriOptions = new String[17];
            aprioriOptions[0] = "-N";
            aprioriOptions[1] = String.valueOf(numberOfSuggestions); //number of rules to return
            aprioriOptions[2] = "-T";
            aprioriOptions[3] = "0";
            aprioriOptions[4] = "-C"; //choose Confidence as metric
            aprioriOptions[5] = String.valueOf(confidence); //minimum of metric (confidence) for a rule to be returned
            aprioriOptions[6] = "-D"; //delta
            aprioriOptions[7] = "0.05"; //decrease every time by substracy 0.05 (delta)
            aprioriOptions[8] = "-U"; //upper bound
            aprioriOptions[9] = "1.0"; //1.0 of upperbound (start point)
            aprioriOptions[10] = "-M"; //min sup
            aprioriOptions[11] = "0.006"; //0.1 of min sup (end point) always
            aprioriOptions[12] = "-S"; //significance
            aprioriOptions[13] = "-1.0";
            aprioriOptions[14] = "-Z";
            aprioriOptions[15] = "-c";
            aprioriOptions[16] = "-1";

            apriori.setOptions(aprioriOptions);
            apriori.buildAssociations(wekaBasketDatabase);

            AssociationRules associationRules = apriori.getAssociationRules();
            for (AssociationRule associationRule : associationRules.getRules()) {
                //get lift metric to decide if return the rule or not
                double lift = associationRule.getNamedMetricValue("Lift");
                if(lift>1.20) {
                    Platform.runLater(() -> {
                        homePaneController.addNewSuggestion(associationRule);
                    });
                }

            }

        } catch (Exception e) {
            Platform.runLater(() -> {
                homePaneController.setProgressIndicatorBasketAnalysisVisibility(false);
            });
            e.printStackTrace();
        }

    }


    //use FP-Growth to find rules
    private void findRulesUsingFPGrowth(Instances wekaBasketDatabase) {

        try {
            System.out.println("start FP-Growth");
            //start FPGrowth...
            FPGrowth fpGrowth = new FPGrowth();
            String[] aprioriOptions = new String[16];

            aprioriOptions[0] = "-P";
            aprioriOptions[1] = "2";
            aprioriOptions[2] = "-I";
            aprioriOptions[3] = "-1";
            aprioriOptions[4] = "-N";
            aprioriOptions[5] = String.valueOf(numberOfSuggestions); //number of rules to return
            aprioriOptions[6] = "-T";
            aprioriOptions[7] = "0";
            aprioriOptions[8] = "-C"; //confidence metric
            aprioriOptions[9] = String.valueOf(confidence);
            aprioriOptions[10] = "-D"; //delta
            aprioriOptions[11] = "0.05"; //decrease every time by substracy 0.05 (delta)
            aprioriOptions[12] = "-U"; //upper bound support
            aprioriOptions[13] = "1.0";
            aprioriOptions[14] = "-M"; //minimum support
            aprioriOptions[15] = "0.003";

            fpGrowth.setOptions(aprioriOptions);
            fpGrowth.buildAssociations(wekaBasketDatabase);

            AssociationRules associationRules = fpGrowth.getAssociationRules();
            for (AssociationRule associationRule : associationRules.getRules()) {

                //get lift metric to decide if return the rule or not
                double lift = associationRule.getNamedMetricValue("Lift");
                if(lift>1) {
                    Platform.runLater(() -> {
                        homePaneController.addNewSuggestion(associationRule);
                    });
                }

            }

        } catch (Exception e) {
            Platform.runLater(() -> {
                homePaneController.setProgressIndicatorBasketAnalysisVisibility(false);
            });
            e.printStackTrace();
        }

    }

    //load basket dataset, do data preprocessing and finally transform dataset for weka Apriori
    private ArrayList<ArrayList<String>> loadBasketDatabase() {

        System.out.println("reading dataset...");

        //this is the transactional dataset that will be prepared for weka
        ArrayList<ArrayList<String>> wekaTransactionalDataset = new ArrayList<>();

        //this is the interval of date we will accept as interest
        Instant now = Instant.now();
        Instant oneWeekAgo = now.minus(7, ChronoUnit.DAYS);

        try {
            File myObj = new File("src\\main\\resources\\com\\giuliofederico\\dataminingproject\\dataset\\basketDataset.txt");
            Scanner myReader = new Scanner(myObj);

            //in data preprocessing we don't consider water and shopping-bag, so not consider the relative attributes
            if(supermarketItemset.contains("water")) { //test to avoid to repeat the same remove every time (causing an exception (-1) )
                supermarketItemset.remove(supermarketItemset.indexOf("water"));
                supermarketItemset.remove(supermarketItemset.indexOf("shopping-bag"));
            }

            //creo modello di riga del dataset di Weka
            String[] rowModel = new String[supermarketItemset.size()];

            //inizialize row model
            for (int i = 0; i < supermarketItemset.size(); i++)
                rowModel[i] = "0"; //missing by default

            long idTransaction = -1;

            //create row using row model
            ArrayList<String> rowTransformedDatabase = new ArrayList<>(Arrays.asList(rowModel));

            //number of current transaction loaded
            int currentNumberOfTransaction = 0;

            while (myReader.hasNextLine()) { //this is the only condition to respect for a record to be considered of interest

                //get transaction
                String[] transaction = myReader.nextLine().split(","); //split to obtain -->rowId, tid, ite purchased, totalPrice, lot, date

                //get tid
                long idCurrentTransaction = Long.valueOf(transaction[1]);

                //get item purchased
                String item = transaction[3];

                //get Date
                Instant purchaseDate = Instant.parse(transaction[6]);

                if (supermarketItemset.contains(item) && ( (purchaseDate.compareTo(oneWeekAgo)>=0 && purchaseDate.compareTo(now)<=0 ) || (currentNumberOfTransaction<=12000))) { //first requirement

                    //data preprocessing-->delete transaction with water and shopping-bag
                    if(item.compareTo("water")!=0
                            && item.compareTo("shopping-bag")!=0) { //second requirement

                        //if the currentTid is the same of idTransaction continue to use the oldest id and continue to insert true (1) where necessary
                        if (idCurrentTransaction == idTransaction) {
                            rowTransformedDatabase.set(supermarketItemset.indexOf(item), "1");

                        } else { //if it's a new transaction...
                            //save the previous
                            if (idTransaction != -1) {
                                wekaTransactionalDataset.add(rowTransformedDatabase);
                                currentNumberOfTransaction++;
                            }


                            //inizializzo il successivo
                            idTransaction = idCurrentTransaction;
                            rowTransformedDatabase = new ArrayList<>(Arrays.asList(rowModel));
                            rowTransformedDatabase.set(supermarketItemset.indexOf(item), "1");
                        }
                    }

                }
            }
            //save the last
            wekaTransactionalDataset.add(rowTransformedDatabase);

            myReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return wekaTransactionalDataset;
    }


    @Override
    protected void succeeded() {
        super.succeeded();
        Platform.runLater(() -> {
            homePaneController.setProgressIndicatorBasketAnalysisVisibility(false);
        });
    }

    @Override
    protected void failed() {
        super.failed();
        Platform.runLater(() -> {
            homePaneController.setProgressIndicatorBasketAnalysisVisibility(false);
        });
    }
}