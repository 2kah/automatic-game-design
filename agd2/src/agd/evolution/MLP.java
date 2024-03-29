package agd.evolution;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Julian Togelius
 * Date: Jul 14, 2008
 * Time: 8:40:41 PM
 */
public class MLP {

    protected double[][] firstConnectionLayer;
    protected double[][] secondConnectionLayer;
    protected double[] hiddenNeurons;
    protected double[] inputs;
    protected double[] outputs;
    protected double mutationMagnitude = 0.1;
    public double learningRate = 0.1;
    private final Random random = new Random();

    public MLP(int numberOfInputs, int numberOfHidden, int numberOfOutputs) {
        this.inputs = new double[numberOfInputs];
        firstConnectionLayer = new double[numberOfInputs][numberOfHidden];
        secondConnectionLayer = new double[numberOfHidden][numberOfOutputs];
        hiddenNeurons = new double[numberOfHidden];
        outputs = new double[numberOfOutputs];
        randomize (0.5);
    }

    public MLP(double [][] firstConnectionLayer, double[][] secondConnectionLayer, int numberOfHidden,
               int numberOfOutputs) {
        this.inputs = new double[firstConnectionLayer.length];
        this.firstConnectionLayer = firstConnectionLayer;
        this.secondConnectionLayer = secondConnectionLayer;
        hiddenNeurons = new double[numberOfHidden];
        outputs = new double[numberOfOutputs];
    }

    private void randomize (double magnitude) {
        randomize (magnitude, firstConnectionLayer);
        randomize (magnitude, secondConnectionLayer);
    }

    private void randomize (double magnitude, double[][] layer) {
        for (int i = 0; i < layer.length; i++) {
            for (int j = 0; j < layer[i].length; j++) {
                layer[i][j] = random.nextGaussian() * 0.5;
            }
        }
    }


    public double[] propagate(double[] inputIn) {
        if (inputs == null) {
            inputs = new double[inputIn.length];
        }
        if (inputs != inputIn) {
            if (inputIn.length > inputs.length)
                System.out.println ("MLP given " + inputIn.length + " inputs, but only intialized for "
                        + inputs.length);
            System.arraycopy (inputIn, 0, this.inputs, 0, inputIn.length);
        }
        if (inputIn.length < inputs.length)
            System.out.println ("NOTE: only " + inputIn.length + " inputs out of " + inputs.length + " are used in the network");
        clear(hiddenNeurons);
        clear(outputs);
        propagateOneStep(inputs, hiddenNeurons, firstConnectionLayer);
        tanh(hiddenNeurons);
        propagateOneStep(hiddenNeurons, outputs, secondConnectionLayer);
        tanh(outputs);
        return outputs;
    }

    public MLP copy() {
        return new MLP(copy(firstConnectionLayer), copy(secondConnectionLayer),
                hiddenNeurons.length, outputs.length);
    }

    public void mutate() {
        mutate(firstConnectionLayer);
        mutate(secondConnectionLayer);
    }

    public void reset() {
        // Uhh... what?
    }

    protected double[][] copy(double[][] original) {
        double[][] copy = new double[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy (original[i], 0, copy[i], 0, original[i].length);
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
        for (int i = 0; i < secondConnectionLayer.length; i++) {
            for (int j = 0; j < secondConnectionLayer[i].length; j++) {
                sum += secondConnectionLayer[i][j];
            }
        }
        return sum;
    }

    public void println() {

        System.out.print("\n\n----------------------------------------------------" +
                "-----------------------------------\n");
        for (int i = 0; i < firstConnectionLayer.length; i++) {

            System.out.print("|");

            for (int j = 0; j < firstConnectionLayer[i].length; j++) {
                System.out.print(" " + firstConnectionLayer[i][j]);
            }

            System.out.print(" |\n");
        }

        System.out.print("----------------------------------------------------" +
                "-----------------------------------\n");


        for (int i = 0; i < secondConnectionLayer.length; i++) {

            System.out.print("|");

            for (int j = 0; j < secondConnectionLayer[i].length; j++) {

                System.out.print(" " + secondConnectionLayer[i][j]);

            }

            System.out.print(" |\n");

        }

        System.out.print("----------------------------------------------------" +
                "-----------------------------------\n");
    }

    private double dtanh(double num) {
        //return 1;
        return (1 - (num * num));

        // for the sigmoid
        //final double val = sig(num);
        //return (val*(1-val));
    }

    public double backPropagate(double[] targetOutputs) {
        // Calculate output error
        double[] outputError = new double[outputs.length];

        for (int i = 0; i < outputs.length; i++) {
            //System.out.println("Node : " + i);
            outputError[i] = dtanh (outputs[i]) * (targetOutputs[i] - outputs[i]);
            //System.out.println("Err: " + (targetOutputs[i] - outputs[i]) +  "=" + targetOutputs[i] +  "-" + outputs[i]);
            //System.out.println("dnet: " +  outputError[i] +  "=" + (dtanh(outputs[i])) +  "*" + (targetOutputs[i] - outputs[i]));

            if (Double.isNaN (outputError[i])) {
                System.out.println ("Problem at output " + i);
                System.out.println (outputs[i] + " " + targetOutputs[i]);
                System.exit (0);
            }
        }

        // Calculate hidden layer error
        double[] hiddenError = new double[hiddenNeurons.length];

        for (int hidden = 0; hidden < hiddenNeurons.length; hidden++) {
            double contributionToOutputError = 0;
            // System.out.println("Hidden: " + hidden);
            for (int toOutput = 0; toOutput < outputs.length; toOutput++) {
                // System.out.println("Hidden " + hidden + ", toOutput" + toOutput);
                contributionToOutputError += secondConnectionLayer[hidden][toOutput] * outputError[toOutput];
                // System.out.println("Err tempSum: " + contributionToOutputError +  "=" +secondConnectionLayer[hidden][toOutput]  +  "*" +outputError[toOutput] );
            }
            hiddenError[hidden] = dtanh (hiddenNeurons[hidden]) * contributionToOutputError;
            //System.out.println("dnet: " + hiddenError[hidden] +  "=" +  dtanh(hiddenNeurons[hidden])+  "*" + contributionToOutputError);
        }

        ////////////////////////////////////////////////////////////////////////////
        //WEIGHTT UPDATE
        ///////////////////////////////////////////////////////////////////////////
        // Update first weight layer
        for (int input = 0; input < inputs.length; input++) {
            for (int hidden = 0; hidden < hiddenNeurons.length; hidden++) {

                double saveAway = firstConnectionLayer[input][hidden];
                firstConnectionLayer[input][hidden] += learningRate * hiddenError[hidden] * inputs[input];

                if (Double.isNaN (firstConnectionLayer[input][hidden])) {
                    System.out.println ("Late weight error! hiddenError " + hiddenError[hidden]
                            + " input " + inputs[input] + " was " + saveAway);
                }
            }
        }

        // Update second weight layer
        for (int hidden = 0; hidden < hiddenNeurons.length; hidden++) {

            for (int output = 0; output < outputs.length; output++) {

                double saveAway = secondConnectionLayer[hidden][output];
                secondConnectionLayer[hidden][output] += learningRate * outputError[output] * hiddenNeurons[hidden];

                if (Double.isNaN (secondConnectionLayer[hidden][output])) {
                    System.out.println ("target: " + targetOutputs[output] + " outputs: " + outputs[output] + " error:" + outputError[output] + "\n" +
                            "hidden: " + hiddenNeurons[hidden] + "\nnew conn weight: " + secondConnectionLayer[hidden][output] + " was: " + saveAway + "\n");
                }
            }
        }

        double summedOutputError = 0.0;
        for (int k = 0; k < outputs.length; k++) {
            summedOutputError += Math.abs (targetOutputs[k] - outputs[k]);
        }
        summedOutputError /= outputs.length;

        // Return something sensible
        return summedOutputError;
    }

    public String info() {
        int numberOfConnections = (firstConnectionLayer.length * firstConnectionLayer[0].length) +
                (secondConnectionLayer.length * secondConnectionLayer[0].length);
        return "Straight mlp, mean connection weight " + (sum() / numberOfConnections);
    }

    public String toString () {
        return "MLP:" + firstConnectionLayer.length + "/"+ secondConnectionLayer.length + "/" + outputs.length;
    }

}
