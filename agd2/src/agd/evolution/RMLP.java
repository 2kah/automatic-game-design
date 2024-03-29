package agd.evolution;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: 15-Aug-2006
 * Time: 19:40:09
 */
public class RMLP {

    protected double[][] firstConnectionLayer;
    protected double[][] recurrentConnectionLayer;
    protected double[][] secondConnectionLayer;
    protected double[] hiddenNeurons;
    protected double[] hiddenNeuronsCopy;
    protected double[] outputs;
    protected double mutationMagnitude = 0.1;

    private final Random random = new Random();

    public RMLP(int numberOfInputs, int numberOfHidden, int numberOfOutputs) {
        firstConnectionLayer = new double[numberOfInputs][numberOfHidden];
        recurrentConnectionLayer = new double[numberOfHidden][numberOfHidden];
        secondConnectionLayer = new double[numberOfHidden][numberOfOutputs];
        hiddenNeurons = new double[numberOfHidden];
        hiddenNeuronsCopy = new double[numberOfHidden];
        outputs = new double[numberOfOutputs];
        randomize (0.5);
    }

    public RMLP(double [][] firstConnectionLayer, double[][] recurrentConnectionLayer,
                        double[][] secondConnectionLayer, int numberOfHidden,
               int numberOfOutputs) {
        this.firstConnectionLayer = firstConnectionLayer;
        this.recurrentConnectionLayer = recurrentConnectionLayer;
        this.secondConnectionLayer = secondConnectionLayer;
        hiddenNeurons = new double[numberOfHidden];
        hiddenNeuronsCopy = new double[numberOfHidden];
        outputs = new double[numberOfOutputs];
    }

    private void randomize (double magnitude) {
        randomize (magnitude, firstConnectionLayer);
        randomize (magnitude, recurrentConnectionLayer);
        randomize (magnitude, secondConnectionLayer);
    }

    private void randomize (double magnitude, double[][] layer) {
        for (int i = 0; i < layer.length; i++) {
            for (int j = 0; j < layer[i].length; j++) {
                layer[i][j] = random.nextGaussian() * 0.5;
            }
        }
    }

    public double[] propagate(double[] inputs) {
        System.arraycopy (hiddenNeurons, 0, hiddenNeuronsCopy, 0, hiddenNeurons.length);
        clear (hiddenNeurons);
        clear (outputs);
        propagateOneStep (inputs, hiddenNeurons, firstConnectionLayer);
        propagateOneStep (hiddenNeuronsCopy, hiddenNeurons, recurrentConnectionLayer);
        tanh (hiddenNeurons);
        propagateOneStep(hiddenNeurons, outputs, secondConnectionLayer);
        tanh (outputs);
        return outputs;
    }

    public RMLP copy() {
        RMLP mlpcopy = new RMLP(copy(firstConnectionLayer), copy (recurrentConnectionLayer),
                copy(secondConnectionLayer), hiddenNeurons.length, outputs.length);
        return mlpcopy;
    }

    public void mutate() {
        mutate (firstConnectionLayer);
        mutate (recurrentConnectionLayer);
        mutate (secondConnectionLayer);
    }

    public void reset() {
        clear (hiddenNeurons);
        clear (hiddenNeuronsCopy);
    }

    protected double[][] copy(double[][] original) {
        double[][] copy = new double[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
                copy[i][j] = original[i][j];
            }
        }
        return copy;
    }

    protected void mutate(double[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] += random.nextGaussian() * mutationMagnitude;
        }
    }

    protected void mutate(double[][] array) {
        for (int i = 0; i < array.length; i++) {
            mutate(array[i]);
        }
    }

    protected void propagateOneStep(double[] fromLayer, double[] toLayer, double[][] connections) {
        for (int from = 0; from < fromLayer.length; from++) {
            for (int to = 0; to < toLayer.length; to++) {
                toLayer[to] += fromLayer[from] * connections[from][to];
            }
        }
    }

    protected void clear(double[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = 0;
        }
    }

    protected void tanh(double[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = Math.tanh(array[i]);
        }
    }

    public double sum() {
        double sum = 0;
        for (int i = 0; i < firstConnectionLayer.length; i++) {
            for (int j = 0; j < firstConnectionLayer[i].length; j++) {
                sum += firstConnectionLayer[i][j];
            }
        }
        for (int i = 0; i < recurrentConnectionLayer.length; i++) {
            for (int j = 0; j < recurrentConnectionLayer[i].length; j++) {
                sum += recurrentConnectionLayer[i][j];
            }
        }
        for (int i = 0; i < secondConnectionLayer.length; i++) {
            for (int j = 0; j < secondConnectionLayer[i].length; j++) {
                sum += secondConnectionLayer[i][j];
            }
        }
        return sum;
    }

    public String info() {
        int numberOfConnections = (firstConnectionLayer.length * firstConnectionLayer[0].length) +
                (recurrentConnectionLayer.length * recurrentConnectionLayer[0].length) +
                (secondConnectionLayer.length * secondConnectionLayer[0].length);
        return "Recurrent mlp, mean connection weight " + (sum() / numberOfConnections);
    }

    public void show(String title, String[][] s) {
        /*NetVisualizer visualizer = new NetVisualizer(new double[][][]{firstConnectionLayer, secondConnectionLayer},
                title, s);  */
    }

    public String toString () {
        return "RecurrentMLP:" + firstConnectionLayer.length + "/"+ secondConnectionLayer.length + "/" + outputs.length;
    }
}
