package DiabetesPrediction;

import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.classifiers.functions.LinearRegression;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
public class DiabetesPrediction {
    static String testFile = "test_diabetes.csv";
    // public static void main(String args[]){
    //     Instances csvdata = loadDataset("diabetes.csv");
    //     resetCSVTestFile(testFile);
    //     writeCSVTestFile(testFile, "1,89,66,23,94,28.1,0.167,21,0\n");
    //     System.out.println(getPrediction(csvdata));
    // }
    public static Instances loadDataset(String fileName) {
        Instances csvdata = null;
        try {
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File("diabetes.csv"));
            csvdata = loader.getDataSet();
            csvdata.setClassIndex(csvdata.numAttributes()-1);
        } catch(IOException e) {
            System.out.println("Unable to find file " + fileName + "\n" + e);
        }
        return csvdata;
    }
    public static void writeCSVTestFile(String fileName, String data) {
        try {
            FileWriter fw = new FileWriter(fileName, true);
            fw.write(data);
            fw.close();
        } catch (IOException e) {
            System.out.println("File write error" + e.getMessage());
        }
    }
    public static void resetCSVTestFile(String fileName) {
        try {
            String header = "";
            char ch = '\0';
            FileReader fr = new FileReader(fileName);
            while((ch = (char)fr.read()) != '\n')
                header += ch;
            fr.close();
            FileWriter fw = new FileWriter(testFile, false);
            fw.write("");
            fw.write(header+"\n");
            fw.close();
        } catch(IOException e) {
            System.out.println("reset file error :" + e.getMessage());
        }
    }
    public static String getPrediction(Instances csvdata) {
        String pred = "";
        try {
            CSVLoader test_loader = new CSVLoader();
            test_loader.setSource(new File(testFile));
            Instances testInst = test_loader.getDataSet();

            LinearRegression lr = new LinearRegression();
            lr.buildClassifier(csvdata);
            for(int i =  0; i < testInst.numInstances(); i ++) {
                pred += lr.classifyInstance(testInst.instance(i)) + "\n";
            }
        } catch(Exception e) {
            System.out.println("prediction error :" + e.getMessage());
        }    
        return pred;
    }
}