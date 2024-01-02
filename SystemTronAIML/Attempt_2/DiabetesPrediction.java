package Attempt_2;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
// import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;
// import weka.classifiers.bayes.NaiveBayes;
// import weka.classifiers.trees.J48;
import weka.classifiers.functions.LinearRegression;

// import java.io.BufferedReader;
import java.io.File;

// import java.io.FileReader;
// import java.io.IOException;
public class DiabetesPrediction {
    public static void main(String args[]){
        try {
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File("diabetes.csv"));
            Instances csvdata = loader.getDataSet();

            csvdata.setClassIndex(csvdata.numAttributes()-1);

            CSVLoader test_loader = new CSVLoader();
            test_loader.setSource(new File("test_diabetes.csv"));
            Instances testInst = test_loader.getDataSet();

            
            // DataSource source = new DataSource("iris.arff");
            // Instances dataset = source.getDataSet();
            
            // dataset.setClassIndex(dataset.numAttributes()-1);
            
            LinearRegression lr = new LinearRegression();
            lr.buildClassifier(csvdata);
            // System.out.println("LinearRegression capabilities: \n"+lr);
            
            // System.out.println(csvdata.toSummaryString());
            
            for(int i =  0; i < testInst.numInstances(); i ++) {
                System.out.println("Prediction : " + lr.classifyInstance(testInst.instance(i)));
            }
            // DataSource source = new DataSource("breast-cancer.arff");
            // Instances dataset = source.getDataSet();
            // // Instances dataset = new Instances(new BufferedReader(new FileReader(new File("breast-cancer.arff"))));

            // System.out.println("Printing dataset:\n" + dataset.toSummaryString());
        }
        catch(Exception e) {
            System.out.println("Error occured!" + e.getMessage());
            e.printStackTrace();
        }
    }
}