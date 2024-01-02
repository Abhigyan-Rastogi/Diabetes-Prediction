package Attempt_1.NeuralNetwork;
import java.lang.Math;

public class Neuron {
    static double alpha = 0.03;
    //weights and biases will be a number from -1 to 1
    public double[] biases, weights;
    public double stored;
    int prevLayerNum = 0;
    boolean isOutput = false;
    public Neuron(int prevLayerNum, boolean isOutput) {
        this.prevLayerNum = prevLayerNum;
        this.isOutput = isOutput;
        biases = new double[prevLayerNum];
        weights = new double[prevLayerNum];
        for(int i = 0; i < prevLayerNum; i ++) {
            biases[i] = 3+Math.random()*3;
            weights[i] = Math.random();
        }
    }
    public void multiply(Neuron[] neurons) {
        double sum = 0;
        for(int i = 0; i < neurons.length; i ++) {
            sum += neurons[i].stored * weights[i] + biases[i];
        }
        if(this.isOutput)
            stored = sum;
        else
            stored = 1/(1+Math.exp(-sum));
        
    }
    public void storeValue(double stored) {
        this.stored = stored;
    }
    public void update(double score) {
        //1 = positive, 0 = good, -1 = negative
        double nb = 0, nw = 0;
        for(int i = 0; i < prevLayerNum; i ++) {
            nb = this.biases[i] + score*(alpha + Math.random()*(1-alpha));
            nw = this.weights[i] + score*(alpha + Math.random()*(1-alpha));
            this.biases[i] = 1/(1+Math.exp(-nb));
            this.weights[i] = 1/(1+Math.exp(-nw));
        }
    }
    public static Neuron[][] makeNetwork(int[] layers) {
        Neuron[][] n = new Neuron[layers.length][];
        for(int i = 0; i < n.length; i ++) {
            n[i] = new Neuron[layers[i]];
        }
        for(int i = 0; i < n[0].length; i ++) {
            n[0][i] = new Neuron(0, false);
        }
        for(int i = 1; i < n.length-1; i ++) {
            for(int j = 0; j < n[i].length; j ++) {
                n[i][j] = new Neuron(layers[i-1], false);
            }
        }
        for(int i = 0; i < n[n.length-1].length; i ++) {
            n[n.length-1][i] = new Neuron(n[n.length-2].length, true);
        }
        return n;
    }
    public static void showNetworkWNB(Neuron[][] n) {
        for(int i = 0; i < n.length; i ++) {
            System.out.println("Layer " + i);
            for(int j = 0; j < n[i].length; j ++) {
                for(int k = 0; k < n[i][j].prevLayerNum; k ++) {
                    System.out.format("(w:%.2f#b:%.2f)\n", n[i][j].weights[k], n[i][j].biases[k]);
                    // System.out.format("(s:%.2f)\n", n[i][j].stored);
                }
                // System.out.println("|");
            }
            System.out.println();
        }
    }
    public static void showNetworkS(Neuron[][] n) {
        for(int i = 0; i < n.length; i ++) {
            System.out.println("Layer " + i);
            for(int j = 0; j < n[i].length; j ++) {
                for(int k = 0; k < n[i][j].prevLayerNum; k ++) {
                    // System.out.format("(w:%.2f#b:%.2f)\n", n[i][j].weights[k], n[i][j].biases[k]);
                    System.out.format("(s:%.2f)\n", n[i][j].stored);
                }
                System.out.println("|");
            }
            System.out.println();
        }
    }
    public static void evaluateInputs(Neuron[][] n, double[] input) {
        if(n[0].length != input.length){
            System.out.println("Invalid number of inputs!");
            return;
        }
        for(int i = 0; i < n[0].length; i ++) {
            n[0][i].storeValue(input[i]);
        }
        for(int i = 1; i < n.length; i ++) {
            for(int j = 0; j < n[i].length; j ++) {
                n[i][j].multiply(n[i-1]);
                // showNetwork(n);
            }
        }
        System.out.println("Showing results!");
        for(int i = 0; i < n[n.length-1].length; i ++) {
            System.out.print(i+": "+n[n.length-1][i].stored);
        }
        System.out.println();
    }
}
