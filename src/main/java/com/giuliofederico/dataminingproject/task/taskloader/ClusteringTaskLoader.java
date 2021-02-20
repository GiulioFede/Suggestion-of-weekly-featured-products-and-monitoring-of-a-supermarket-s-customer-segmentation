package com.giuliofederico.dataminingproject.task.taskloader;

import com.giuliofederico.dataminingproject.controller.HomePaneController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import weka.clusterers.AbstractClusterer;
import weka.clusterers.HierarchicalClusterer;
import weka.clusterers.KValid;
import weka.core.*;
import weka.core.neighboursearch.LinearNNSearch;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.filters.unsupervised.instance.Resample;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class ClusteringTaskLoader extends Task<Parent> {

    private HomePaneController homePaneController;

    public ClusteringTaskLoader(HomePaneController homePaneController) {
        this.homePaneController = homePaneController;

    }


    @Override
    protected Parent call() throws Exception {

        loadDataset();


        return null;
    }

    private void loadDataset() {


        //Attributes ________________________________________________________________________________________
        Attribute custId = new Attribute("customerId");

        Attribute name = new Attribute("name", true);

        Attribute email = new Attribute("email", true);

        ArrayList<String> marriedLabels = new ArrayList<>();
        marriedLabels.add("married");
        marriedLabels.add("unmarried");
        Attribute married = new Attribute("married", marriedLabels);

        ArrayList<String> sexLabels = new ArrayList<>();
        sexLabels.add("Male");
        sexLabels.add("Female");
        Attribute sex = new Attribute("sex", sexLabels);

        Attribute dateOfBirth = new Attribute("date_of_birth", true);

        Attribute age = new Attribute("age");

        ArrayList<String> jobCategoryLabels = new ArrayList<>(Arrays.asList(("Retail,Property,Manufacturing,Health,Financial Services,IT,Entertainment,Agriculture,Telecommunications").split(",")));
        Attribute jobCategory = new Attribute("jobCategory", jobCategoryLabels);

        ArrayList<String> jobLabels = new ArrayList<>(Arrays.asList(("Recruiting Manager,VP Quality Control,Tax Accountant,Environmental Specialist,Executive Secretary,Software Test Engineer II,Structural Engineer,Electrical Engineer,Desktop Support Technician,Librarian,Sales Representative,Human Resources Assistant I,Social Worker,Administrative Officer,Geologist I,Programmer I,Food Chemist,Research Assistant II,Software Test Engineer I,Help Desk Technician,Legal Assistant,VP Marketing,Mechanical Systems Engineer,Software Consultant,Pharmacist,Assistant Professor,Quality Engineer,Account Coordinator,Actuary,Chemical Engineer,Registered Nurse,Recruiter,Safety Technician II,Quality Control Specialist,Data Coordiator,Technical Writer,Junior Executive,Paralegal,Developer I,Senior Financial Analyst,Teacher,Statistician I,Internal Auditor,Research Nurse,Nurse Practicioner,Systems Administrator III,Associate Professor,VP Accounting,Systems Administrator I,Graphic Designer,Developer II,GIS Technical Architect,Compensation Analyst,Software Test Engineer III,Web Developer I,Office Assistant IV,Geological Engineer,Health Coach IV,Help Desk Operator,Dental Hygienist,Business Systems Development Analyst,Safety Technician III,Software Engineer I,Statistician II,Speech Pathologist,Physical Therapy Assistant,Analog Circuit Design manager,Nurse,Account Executive,Senior Cost Accountant,Safety Technician IV,Staff Scientist,Structural Analysis Engineer,VP Sales,VP Product Management,Marketing Manager,Budget/Accounting Analyst II,Accountant IV,Occupational Therapist,Senior Editor,Administrative Assistant IV,Media Manager II,Engineer I,Chief Design Engineer,Developer III,Biostatistician II,Programmer Analyst II,Sales Associate,Operator,Staff Accountant II,Geologist III,Research Associate,Budget/Accounting Analyst I,Clinical Specialist,Senior Quality Engineer,Assistant Manager,Product Engineer,Human Resources Assistant II,Financial Advisor,Account Representative IV,Senior Sales Associate,Account Representative I,Programmer Analyst I,Environmental Tech,Biostatistician I,General Manager,Web Designer IV,Staff Accountant IV,Cost Accountant,Civil Engineer,Engineer IV,Administrative Assistant III,Accountant III,Analyst Programmer,Assistant Media Planner,Editor,Nuclear Power Engineer,Safety Technician I,Computer Systems Analyst II,Senior Developer,Accounting Assistant IV,Project Manager,Administrative Assistant II,Director of Sales,Software Engineer III,Health Coach III,Software Engineer IV,Office Assistant I,Web Designer II,Community Outreach Specialist,Statistician III,Financial Analyst,Payment Adjustment Coordinator,Automation Specialist II,Budget/Accounting Analyst IV,Staff Accountant III,Programmer III,Design Engineer,Human Resources Assistant IV,Accounting Assistant II,Engineer II,Systems Administrator IV,Professor,Web Developer II,Budget/Accounting Analyst III,Programmer II,Research Assistant I,Web Developer III,Media Manager I,Information Systems Manager,Account Representative III,Systems Administrator II,Computer Systems Analyst I,Administrative Assistant I,Computer Systems Analyst IV,Marketing Assistant,Staff Accountant I,Accountant II,Database Administrator III,Automation Specialist IV,Web Designer I,Biostatistician IV,Geologist II,Database Administrator IV,Human Resources Assistant III,Database Administrator II,Health Coach II,Programmer Analyst III,Account Representative II,Biostatistician III,Human Resources Manager,Accountant I,Accounting Assistant I,Research Assistant III,Statistician IV,Health Coach I,Software Test Engineer IV,Database Administrator I,Office Assistant III,Software Engineer II,Media Manager III,Accounting Assistant III,Automation Specialist III,Web Designer III,Developer IV,Web Developer IV,Programmer Analyst IV,Media Manager IV,Research Assistant IV,Engineer III,Programmer IV,Automation Specialist I,Computer Systems Analyst III,Geologist IV,Office Assistant II").split(",")));
        Attribute job = new Attribute("job", jobLabels);

        ArrayList<String> statusLabels = new ArrayList<>(Arrays.asList(("Affluent Customer,Mass Customer,High Net Worth").split(",")));
        Attribute status = new Attribute("status", statusLabels);

        Attribute income = new Attribute("income");

        Attribute annualExpenses = new Attribute("annualExpenses");

        //9 attributes for supermarket food categories
        //                    beverage //beveraggio
        //                    bakery  //dolci
        //                    cannedFoods //cibi in scatola
        //                    dryFoods //cibi secchi: es. cereali
        //                    frozenFoods //cibi congelati
        //                    meat //carne
        //                    produce  //verdure e frutta
        //                    cleaners  //pulizia
        //                    others //
        Attribute beverage = new Attribute("beverage");
        Attribute bakery = new Attribute("backery");
        Attribute cannedFoods = new Attribute("cannedFoods");
        Attribute dryFoods = new Attribute("dryFoods");
        Attribute frozenFoods = new Attribute("frozenFoods");
        Attribute meat = new Attribute("meat");
        Attribute produce = new Attribute("produce");
        Attribute cleaners = new Attribute("cleaners");
        Attribute others = new Attribute("others");

        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(custId);
        attributes.add(name);
        attributes.add(email);
        attributes.add(married);
        attributes.add(sex);
        attributes.add(dateOfBirth);
        attributes.add(age);
        attributes.add(job);
        attributes.add(jobCategory);
        attributes.add(status);
        attributes.add(income);
        attributes.add(annualExpenses);
        attributes.add(beverage);
        attributes.add(bakery);
        attributes.add(cannedFoods);
        attributes.add(dryFoods);
        attributes.add(frozenFoods);
        attributes.add(meat);
        attributes.add(produce);
        attributes.add(cleaners);
        attributes.add(others);

        //postpone the creation of database to allocate the right space just one time (avoiding multiples reallocations), for now create only the instances container
        ArrayList<Instance> instances = new ArrayList<>();

        //read customerDatabase
        try {

            File myObj = new File("src\\main\\resources\\com\\giuliofederico\\dataminingproject\\dataset\\customerDataset.txt");
            Scanner myReader = new Scanner(myObj);

            //datta formatter
            SimpleDateFormat parser = new SimpleDateFormat("MM-dd-yyyy:HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            LocalDate today = LocalDate.now();

            while (myReader.hasNextLine()) {

                String[] data = myReader.nextLine().split(",");

                //(age) data preprocessing check
                Date date = parser.parse(parseDate(data[5], "EEE MMM dd HH:mm:ss zzz yyyy", "MM-dd-yyyy:HH:mm:ss"));
                calendar.setTime(date);
                LocalDate dateOfBirthday = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH));
                int ageOfCustomer = Period.between(dateOfBirthday, today).getYears();

                //sex
                if(data[4].compareTo("")==0)
                    continue; //i decided to delete tuple, it doesn't not make sense use a the mean of sex


                double[] values = new double[data.length];
                System.out.println(data.toString());
                values[0] = Double.valueOf(data[0]);
                values[1] = name.addStringValue(data[1]);
                values[2] = email.addStringValue(data[2]);

                //marital status
                if(data[3].compareTo("")==0 || marriedLabels.contains(data[3])==false)
                    values[3] = Utils.missingValue();
                else
                    values[3] = married.indexOfValue(data[3]);


                values[4] = sex.indexOfValue(data[4]);
                values[5] = dateOfBirth.addStringValue(data[5]);

                //if age and date of birth doesn't match -->replace age with missing vale (it will be re-replaced with a mean. Se later)
                if (Integer.valueOf(data[6].substring(0, 2)) == ageOfCustomer)
                    values[6] = Double.valueOf(data[6]);
                else
                    values[6] = Utils.missingValue();

                values[7] = job.indexOfValue(data[7]);

                //check job category
                if(data[8].compareTo("")==0 || jobCategoryLabels.contains(data[8])==false)
                    continue;
                else
                    values[8] = jobCategory.indexOfValue(data[8]);

                //check status
                if(data[9].compareTo("")==0 || statusLabels.contains(data[9])==false)
                    values[9]= Utils.missingValue();
                else
                    values[9] = status.indexOfValue(data[9]);

                values[10] = Double.valueOf(data[10]);
                values[11] = Double.valueOf(data[11]);

                if(Double.valueOf(data[12])<0 || Double.valueOf(data[12])>1)
                    values[12] = Utils.missingValue();
                else
                    values[12] = Double.valueOf(data[12]);

                if(Double.valueOf(data[13])<0 || Double.valueOf(data[13])>1)
                    values[13] = Utils.missingValue();
                else
                    values[13] = Double.valueOf(data[13]);

                if(Double.valueOf(data[14])<0 || Double.valueOf(data[14])>1)
                    values[14] = Utils.missingValue();
                else
                    values[14] = Double.valueOf(data[14]);

                if(Double.valueOf(data[15])<0 || Double.valueOf(data[15])>1)
                    values[15] = Utils.missingValue();
                else
                    values[15] = Double.valueOf(data[15]);

                if(Double.valueOf(data[16])<0 || Double.valueOf(data[16])>1)
                    values[16] = Utils.missingValue();
                else
                    values[16] = Double.valueOf(data[16]);

                if(Double.valueOf(data[17])<0 || Double.valueOf(data[17])>1)
                    values[17] = Utils.missingValue();
                else
                    values[17] = Double.valueOf(data[17]);

                if(Double.valueOf(data[18])<0 || Double.valueOf(data[18])>1)
                    values[18] = Utils.missingValue();
                else
                    values[18] = Double.valueOf(data[18]);

                if(Double.valueOf(data[19])<0 || Double.valueOf(data[19])>1)
                    values[19] = Utils.missingValue();
                else
                    values[19] = Double.valueOf(data[19]);

                if(Double.valueOf(data[20])<0 || Double.valueOf(data[20])>1)
                    values[20] = Utils.missingValue();
                else
                    values[20] = Double.valueOf(data[20]);




                instances.add(new DenseInstance(1.0, values));

            }
            myReader.close();

            //create first dataset for weka
            Instances wekaRawDataset = new Instances("customerDataset", attributes, instances.size());

            //add instances
            for (Instance inst : instances) {
                wekaRawDataset.add(inst);
            }

            //FILTERING::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

            //remove filter
            Remove removeAttributes = new Remove();
            String[] removeAttributesOptions = new String[2];
            removeAttributesOptions[0] = "-R";
            removeAttributesOptions[1] = "1,2,3,6,8";
            removeAttributes.setOptions(removeAttributesOptions);
            removeAttributes.setInputFormat(wekaRawDataset);
            Instances wekaDatasetAfterRemoveAttributes = Filter.useFilter(wekaRawDataset, removeAttributes);

            //replace attribute with mode (if nominal) or mean (if numeric)
            ReplaceMissingValues replaceMissingValues = new ReplaceMissingValues();
            replaceMissingValues.setInputFormat(wekaDatasetAfterRemoveAttributes);
            Instances wekaDatasetAfterReplaceMissingValues = Filter.useFilter(wekaDatasetAfterRemoveAttributes, replaceMissingValues);

            //normalize into the same scale [0,1]
            Normalize normalizeNumericAttributes = new Normalize();
            String[] normalizeNumericAttributesOptions = new String[4];
            normalizeNumericAttributesOptions[0] = "-S";
            normalizeNumericAttributesOptions[1] = "1.0";
            normalizeNumericAttributesOptions[2] = "-T";
            normalizeNumericAttributesOptions[3] = "0.0";
            normalizeNumericAttributes.setOptions(normalizeNumericAttributesOptions);
            normalizeNumericAttributes.setInputFormat(wekaDatasetAfterReplaceMissingValues);
            Instances wekaDataset = Filter.useFilter(wekaDatasetAfterReplaceMissingValues, normalizeNumericAttributes);

            //automatically class no setted, ok!


            //mining
            //K-VALID (find best k for K-Means)_________________________________________________________________________
            //check cluster tendency if you want
            /*computeClusterTendencyWithHopkins(wekaDataset,
                    marriedLabels,
                    sexLabels,
                    jobCategoryLabels,
                    statusLabels);

             */
            startKValid(wekaDataset, wekaRawDataset); //pass the second because when i want to show results i need row data

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            stopLoading();
        } catch (Exception e) {
            e.printStackTrace();
            stopLoading();
        }

    }

    private void computeClusterTendencyWithHopkins(Instances dataset,
                                                   ArrayList<String> marriedLabels,
                                                   ArrayList<String> sexLabels,
                                                   ArrayList<String> jobCategoryLabels,
                                                   ArrayList<String> statusLabels) {


        try {
            //second: sample dataset of m examples_________________________________
            Resample resample = new Resample();
            String[] resampleOptions = new String[5];
            resampleOptions[0] = "-S";
            resampleOptions[1] = "1";
            resampleOptions[2] = "-Z";
            resampleOptions[3] = "10.0"; //%10
            resampleOptions[4] = "-no-replacement";
            resample.setOptions(resampleOptions);
            resample.setInputFormat(dataset);
            Instances examples = Filter.useFilter(dataset,resample);

            //3°: compute the distances with their nearest neighbor
            ArrayList<Double> examplesDistances = new ArrayList<>();
            //declare type of search
            LinearNNSearch linearNNSearch = new LinearNNSearch(dataset);
            //declare type of distance
            EuclideanDistance euclideanDistance = new EuclideanDistance(dataset);
            for(int i=0; i<examples.numInstances(); i++){
                //target instance
                Instance target = examples.instance(i);
                //nearest neighbor
                Instance nearest = linearNNSearch.kNearestNeighbours(target,2).instance(1);

                //distance from it
                double dist = euclideanDistance.distance(target,nearest);
                //add
                examplesDistances.add(dist);
            }

            //first: generate random synthetic instances_______________________________
            //We decide the cardinality as m = 10% of the original dataset
            int cardinalitySyntheticDataset = (int)(0.1*dataset.numInstances());
            ArrayList<Instance> syntheticInstances = new ArrayList<>();

            Random random = new Random();
            ArrayList<Attribute> attributes = new ArrayList<>();
            Enumeration<Attribute> enumeration = dataset.enumerateAttributes();
            while (enumeration.hasMoreElements())
                attributes.add(enumeration.nextElement());

            for(int i=0; i<cardinalitySyntheticDataset; i++){


                double[] values = new double[dataset.numAttributes()];

                values[0] = attributes.get(0).addStringValue(marriedLabels.get(random.nextInt(2)));
                values[1] = attributes.get(1).addStringValue(sexLabels.get(random.nextInt(2)));
                values[2] = random.nextDouble();
                values[3] = attributes.get(3).addStringValue(jobCategoryLabels.get(random.nextInt(9)));
                values[4] = attributes.get(4).addStringValue(statusLabels.get(random.nextInt(3)));
                values[5] = random.nextDouble();
                values[6] = random.nextDouble();
                values[7] = random.nextDouble();
                values[8] = random.nextDouble();
                values[9] = random.nextDouble();
                values[10] = random.nextDouble();
                values[11] = random.nextDouble();
                values[12] = random.nextDouble();
                values[13] = random.nextDouble();
                values[14] = random.nextDouble();
                values[15] = random.nextDouble();

                //add instance also to dataset (otherwise the search of knn doesn't work)
                Instance syntheticInstance = new DenseInstance(1.0, values);
                dataset.add(syntheticInstance);
                syntheticInstance.setDataset(dataset);

                syntheticInstances.add(syntheticInstance);
            }

            //3°: compute the distances of syntethic data with their nearest neighbor
            ArrayList<Double> syntheticDistances = new ArrayList<>();

            for(int i=0; i<syntheticInstances.size(); i++){
                //taget instance
                Instance target = syntheticInstances.get(i);
                //nearest neighbor
                Instance nearest = linearNNSearch.kNearestNeighbours(target,2).instance(1);
                //distance from it
                double dist = euclideanDistance.distance(target,nearest);
                //add
                syntheticDistances.add(dist);
            }

            //compute Hopkins index
            double synteticSum = syntheticDistances.stream()
                    .mapToDouble(a -> a)
                    .sum();

            double examplesSum = examplesDistances.stream()
                    .mapToDouble(a -> a)
                    .sum();

            double H = synteticSum / (synteticSum + examplesSum);
            System.out.println("Hopkins: "+H);



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startKValid(Instances wekaDataset, Instances wekaDatasetDenormalized) {

        try {
            KValid kValid = new KValid();
            String[] kvalidOptions = ("-init,0,-N,3,-A,weka.core.EuclideanDistance -R first-last,-I,500,-validation,0,-cascade,-minK,2,-maxK,10,-show-graph,-S,5").split(",");
            kValid.setOptions(kvalidOptions);
            kValid.buildClusterer(wekaDataset);
            //get top k
            System.out.println(kValid.numberOfClusters());
            //it's already possible to get cluster instead of repeating K-Means with the k founded
            //so use this cluster to clustering instances
            for (int j = 0; j < wekaDataset.numInstances(); j++) {

                //get number of cluster beloging
                System.out.println(j + ":" + kValid.clusterInstance(wekaDataset.instance(j)));
            }

            //compute silhouette index and give it to the user as a reliability of clustering
            double reliability = computeSilhouetteIndex(wekaDataset, kValid);

            returnClusteringResult(wekaDataset, wekaDatasetDenormalized, kValid, reliability);

        } catch (Exception e) {
            e.printStackTrace();
            stopLoading();
        }
    }

    private double computeSilhouetteIndex(Instances wekaDataset, AbstractClusterer clustering) {

        //compute silhuette index:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        EuclideanDistance euclideanDistance = new EuclideanDistance(wekaDataset);
        //container of all silhouette indexes
        ArrayList<Double> silhouetteIndexes = new ArrayList<>();
        for (int n = 0; n < wekaDataset.numInstances(); n++) { //for each object in the dataset...
            try {
                //compute a(o) and b(0)__________________________________
                //useful for computing a(o)
                Instance myInstance = wekaDataset.instance(n);
                int myCluster = clustering.clusterInstance(myInstance);
                double a = 0; //a coeff
                int myClusterCardinality = 0;

                //useful for computing b(0)
                double b = 0; //b coeff
                double[] distancesFromOtherClusters = new double[clustering.numberOfClusters()]; //mantain distances from other clusters
                int[] cardinalityOfOtherClusters = new int[clustering.numberOfClusters()]; //maintain cardinality of other clusters


                //start...
                for (int m = 0; m < wekaDataset.numInstances(); m++) {

                    Instance instance = wekaDataset.instance(m);

                    if (n != m) {
                        int clusterInstance = clustering.clusterInstance(instance);
                        //computing a(0)...
                        if (myCluster == clusterInstance) {
                            a += euclideanDistance.distance(myInstance, instance);
                            myClusterCardinality++;
                        } else {//computing b(o)...
                            cardinalityOfOtherClusters[clusterInstance] += 1;
                            distancesFromOtherClusters[clusterInstance] += euclideanDistance.distance(myInstance, instance);
                        }
                    }


                }

                //final computation of a(o)
                a = a / (myClusterCardinality - 1);

                //final computation of b(o)
                for (int c = 0; c < cardinalityOfOtherClusters.length; c++)
                    distancesFromOtherClusters[c] = distancesFromOtherClusters[c] / cardinalityOfOtherClusters[c];
                Arrays.sort(distancesFromOtherClusters);
                b = distancesFromOtherClusters[0];

                //compute silhouette index of the current instance and add to a list
                double silhouetteIndex = (b - a) / Math.max(a, b);
                silhouetteIndexes.add(silhouetteIndex);
                System.out.println("silhouette of instance " + n + " :" + silhouetteIndex);


            } catch (Exception e) {
                e.printStackTrace();
                stopLoading();
            }

        }


        //compute quality of overall clustering
        double quality = (silhouetteIndexes.stream().mapToDouble(s -> s.doubleValue()).sum()) / silhouetteIndexes.size();
        System.out.println("Quality of clustering: " + quality);
        return quality;

    }

    private void returnClusteringResult(Instances dataset, Instances datasetDenormalized, AbstractClusterer clustering, double quality) {

        Platform.runLater(() -> {
            homePaneController.processClusteringResult(dataset, datasetDenormalized, clustering, quality);
        });
    }

    public String parseDate(String dateTime, String inputPattern, String outputPattern) throws ParseException {

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = inputFormat.parse(dateTime);
        String str = outputFormat.format(date);

        return str;
    }


    private void stopLoading(){
        Platform.runLater(() -> {
            homePaneController.setProgressIndicatorClusteringAnalysisVisibility(false);
            homePaneController.setStartClusteringButtonVisibility(true);
        });
    }


    @Override
    protected void failed() {
        super.failed();
        stopLoading();
    }


    //DBSCAN::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    private class Pair implements Comparable<Pair> {
        private int numberOfInstance;
        private double distanceWithTheKthNearestNeighbor;

        public Pair(int numberOfInstance, double distanceWithTheKthNearestNeighbor) {
            this.numberOfInstance = numberOfInstance;
            this.distanceWithTheKthNearestNeighbor = distanceWithTheKthNearestNeighbor;
        }

        public int getNumberOfInstance() {
            return numberOfInstance;
        }

        public void setNumberOfInstance(int numberOfInstance) {
            this.numberOfInstance = numberOfInstance;
        }

        public double getDistanceWithTheKthNearestNeighbor() {
            return distanceWithTheKthNearestNeighbor;
        }

        public void setDistanceWithTheKthNearestNeighbor(double distanceWithTheKthNearestNeighbor) {
            this.distanceWithTheKthNearestNeighbor = distanceWithTheKthNearestNeighbor;
        }

        @Override
        public int compareTo(Pair o) {
            if(this.distanceWithTheKthNearestNeighbor>o.getDistanceWithTheKthNearestNeighbor())
                return 1;
            else if(this.distanceWithTheKthNearestNeighbor==o.getDistanceWithTheKthNearestNeighbor())
                return 0;
            else
                return -1;

        }
    }

    //:::::::::::::DENSITY-BASED (an example of code approach):::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //only particular methods

    public void computeBestParametersForDBSCAN(Instances dataset){

        int k = dataset.numAttributes()*2;

        //declare type of search
        LinearNNSearch linearNNSearch = new LinearNNSearch();
        //declare type of distance
        EuclideanDistance euclideanDistance = new EuclideanDistance(dataset);
        //declare array that will contains all the k-th distance for each point
        ArrayList<Pair> kthDistances = new ArrayList<>();
        System.out.println("KNEAREST NEIGHBORS::::::::::::::::::::::::");
        try {
            linearNNSearch.setInstances(dataset);

            //for each instance compute its k-th nearest neighbor
            int  i=0;
            for(Instance target : dataset) {
                //fin k nearest neighbors of the target instance
                Instances knearest = linearNNSearch.kNearestNeighbours(target,k);
                //calculate the distance to the farthest
                double kdist = euclideanDistance.distance(target,knearest.instance(k-1));
                //save it
                kthDistances.add(new Pair(i,kdist));
                i++;
            }

            //sort in descending order respects to the kth-distance (already compareTo() method overrided)
            Collections.sort(kthDistances,Collections.reverseOrder());

            //int middleInstance = HSLSAlgorithm(kthDistances);
            int middleInstance = equitySlope(kthDistances);
            //best parameter
            double eps = kthDistances.get(middleInstance).distanceWithTheKthNearestNeighbor;
            System.out.println("Best parameters k="+k+"  and eps="+eps+" middlepoint= "+middleInstance);


            //start DBSCAN
            //...
            System.out.println();

        }catch (Exception e) {
            e.printStackTrace();
        }


    }


    //Higher Slope Lower Speed Algorithm: versione più semplice (ma meno efficace) di EquitySlope
    public int HSLSAlgorithm(ArrayList<Pair> points){


        int leftPoint = 0;
        int rightPoint = points.size()-1;

        while(true){

            if(leftPoint== points.size()-1)
                break;
            if(rightPoint==1)
                break;

            if(leftPoint>rightPoint)
                break;

            double lslop = points.get(leftPoint).distanceWithTheKthNearestNeighbor - points.get(leftPoint+1).distanceWithTheKthNearestNeighbor;
            double rslop = points.get(rightPoint-1).distanceWithTheKthNearestNeighbor-points.get(rightPoint).distanceWithTheKthNearestNeighbor;

            System.out.println("PASSO: "+getHop(lslop,rslop));
            if(lslop>rslop) {
                leftPoint += 1;
                rightPoint -= getHop(lslop,rslop);
            }else {
                leftPoint+=getHop(lslop,rslop);
                rightPoint-=1;
            }


        }

        return Math.min(leftPoint,rightPoint);
    }

    private int getHop(double lslop, double rslop){

        double rapp =  Math.min(lslop,rslop)/ Math.max(lslop,rslop);
        if(rapp<0.1)
            return 10;
        if(rapp<0.2)
            return 9;
        if(rapp<0.3)
            return 8;
        if(rapp<0.4)
            return 7;
        if(rapp<0.5)
            return 6;
        if(rapp<0.6)
            return 5;
        if(rapp<0.7)
            return 4;
        if(rapp<0.8)
            return 3;
        if(rapp<0.9)
            return 2;
        else
            return 1;
    }

    //EquitySlope
    public int equitySlope(ArrayList<Pair> points){


        int leftPoint = 0;
        int rightPoint = points.size()-1;

        while(true){

            if(leftPoint== points.size()-1)
                break;
            if(rightPoint==1)
                break;

            if(leftPoint>=rightPoint)
                break;

            //how much will it go down?
            double lslop = points.get(leftPoint).distanceWithTheKthNearestNeighbor - points.get(leftPoint+1).distanceWithTheKthNearestNeighbor;
            double rslop = points.get(rightPoint-1).distanceWithTheKthNearestNeighbor-points.get(rightPoint).distanceWithTheKthNearestNeighbor;

            int hop = 1;
            //System.out.println("lslop: "+lslop+"  rslop: "+rslop);
            System.out.println("l: "+leftPoint+"  rs: "+rightPoint);
            if(rslop<lslop){
                while(true){
                    System.out.println("hop: "+hop);
                    if( (points.get(rightPoint-hop).distanceWithTheKthNearestNeighbor-points.get(rightPoint).distanceWithTheKthNearestNeighbor)>=lslop)
                        break;
                    if(rightPoint + hop==leftPoint)
                        break;
                    hop++;
                }

            }

            leftPoint++;
            rightPoint-=hop;


        }

        return rightPoint;
    }

    //::::::::::::::HIERARCHICAL (an example of code approach):::::::::::::::::::::::::::::::::::::::::::

    public static String[] TYPE_OF_LINKAGE = {"SINGLE","COMPLETE","MEAN","AVERAGE"};

    public void clusteringWithHierarchicalApproach(Instances dataset){

        //test
        HierarchicalClusterer hierarchicalClusterer = new HierarchicalClusterer();
        String[] hierarchicalClusterOptions = ("-N,3,-L,SINGLE-P,-A,weka.core.EuclideanDistance -R first-last").split(",");
        try {
            hierarchicalClusterer.setOptions(hierarchicalClusterOptions);
            hierarchicalClusterer.buildClusterer(dataset);
        } catch (Exception e) {
            e.printStackTrace();
        }

        computeSilhouetteIndex(dataset,hierarchicalClusterer);


        }


}