package Attempt_1;

import java.io.IOException;
import java.io.*;
import java.util.*;

import Attempt_1.NeuralNetwork.*;

public class DiabetesPrediction {
    public static void main(String args[]) {
        Data data = new Data("numerical information.csv");
        Neuron[][] n = trainPrediction(new int[] {2, 4, 4, 1}, data.type);
        // Neuron.showNetworkS(n);
        // Neuron.showNetworkWNB(n);
        Neuron.evaluateInputs(n, new double[]{30, 180});
        startPrediction();

        // Neuron[][] n = Neuron.makeNetwork(new int[]{2,3,3,1});
        // Trainer train = new Trainer(new double[]{0.3141526}, 100);
        // train.trainNeuralNetwork(n);      
    }
    public static Neuron[][] trainPrediction(int[] arch, String[][] data) {
        Neuron[][] n = Neuron.makeNetwork(arch);
        Trainer train = new Trainer(2);
        train.trainNeuralNetwork(n, data, 2);
        return n;
    }
    public static void startPrediction() {
        System.out.println("Reading objectives file : ");
        Data data = new Data("numerical information.csv");
        data.showData();
    }
    /*
     * Reading the CSV file as: 
     * First line: data headings
     * Other line: data
     * 
     * making it so that we can easily change number of data headings used
     */
    public static class Data {
        String[][] type = new String[1000][100];
        int numHeads = 0, numLines = 1;
        Data(String filePath) {
            String in = readCSVFile(filePath);
            StringTokenizer lines = new StringTokenizer(in, "\n", false);
            if(lines.hasMoreTokens()) {
                StringTokenizer headings = new StringTokenizer(lines.nextToken(), ",", false);
                while(headings.hasMoreTokens()) {
                    type[0][numHeads++] = headings.nextToken();
                    if(numHeads > type[0].length) {
                        String[][] temp = new String[2*numHeads][100];
                        for(int i = 0; i < numHeads; i ++) {
                            temp[i][0] = type[i][0];
                        }
                        type = temp;
                    }
                }
            }
            while(lines.hasMoreTokens()) {
                StringTokenizer curLine = new StringTokenizer(lines.nextToken(), ",", false);
                for(int i = 0; i < numHeads; i ++) {
                    if(curLine.hasMoreTokens())
                        type[numLines][i] = curLine.nextToken();
                    else
                        type[numLines][i] = "";
                }
                numLines++;
            }
            String[][] temp = new String[numLines][numHeads];
            for(int i = 0; i < numLines; i ++) {
                for(int j = 0; j < numHeads; j ++) {
                    temp[i][j] = type[i][j];
                }
            }
            type = temp;
        }
        public static String readCSVFile(String filePath) {
            String ret = "";
            try {
                FileInputStream fis = new FileInputStream(filePath);
                Scanner sc = new Scanner(fis, "UTF-8");
                while(sc.hasNextLine()) {
                    ret += sc.nextLine() + "\n";
                }
                sc.close();
            }
            catch(IOException e) {
                System.out.println("File not found : " + e);
                e.printStackTrace();
            }
            return ret;
        }
        public void showData() {
            for(int i = 0; i < numLines; i ++) {
                for(int j = 0; j < numHeads; j ++) {
                    System.out.format("%10.10s|", type[i][j]);
                }
                System.out.println();
            }
        }
    }
}