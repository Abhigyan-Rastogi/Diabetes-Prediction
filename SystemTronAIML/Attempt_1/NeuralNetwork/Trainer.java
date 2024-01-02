package Attempt_1.NeuralNetwork;

public class Trainer {
    int iterations;
    public Trainer(int iterations) {
        this.iterations = iterations;
    }
    public void trainNeuralNetwork(Neuron[][] n, String[][] data, int output) {
        double[][] trainingData = digestStringData(data);
        trainNeuralNetwork(n, trainingData, output);
    }
    public void trainNeuralNetwork(Neuron[][] n, double[][] trainingData, int outfield) {
        int cur = 0;
        //assuming input layer has (n-1) nodes, where n is number of fields in training data. One is left for output to be predicted.
        //assuming output layer has 1 node
        while(cur < trainingData.length) {
            double expOut = trainingData[cur][outfield];

            for(int i = 0; i < n[0].length; i ++) {
                if(i == outfield)
                    n[0][i].storeValue(trainingData[cur][(i+1)%trainingData[0].length]);
                else
                    n[0][i].storeValue(trainingData[cur][i]);
            }

            for(int k = 0; k < iterations; k ++) {
                for(int i = 1; i < n.length; i ++) {
                    for(int j = 0; j < n[i].length; j ++) {
                        n[i][j].multiply(n[i-1]);
                    }
                }

                double output = n[n.length-1][0].stored;
                double score = expOut - output;

                for(int i = 1; i < n.length; i ++) {
                    for(int j =  0; j < n[i].length; j ++) {
                        n[i][j].update(score);
                    }
                }
                // Neuron.showNetworkWNB(n);
            }
            cur ++;
        }
    }
    //assuming all numerical information
    public static double[][] digestStringData(String[][] data) {
        double[][] ret = new double[data.length][data[0].length];
        for(int i = 1; i < data.length; i ++) {
            for(int j = 0; j < data[i].length; j ++) {
                if(data[i][j].equals(""))
                    ret[i][j] = 0;
                else
                    ret[i][j] = Double.parseDouble(data[i][j]);
            }
        }
        return ret;
    }
}
