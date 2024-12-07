package Model;

import Domain.Instance;
import Domain.Model;

import java.util.List;
//Perceptron Concept
////The Perceptron is a type of linear classifier and one of the simplest types of artificial neural networks.
// It is used for binary classification tasks. The key idea is to find a linear decision boundary that separates instances of different classes.
// The steps involved in the Perceptron algorithm are:
////Initialization: Initialize the weights and bias to small random values or zeros.
////        Training: For each instance in the training data, perform the following steps:
////Compute the weighted sum of the input features plus the bias.
////Apply the activation function (usually the sign function) to determine the predicted class.
////Update the weights and bias if the prediction is incorrect.
////Prediction: For a new instance, compute the weighted sum of the input features plus the bias and apply the activation function to determine the predicted class.

public class Perceptron implements Model<Double,Integer> {
    private double[] weights;
    private double bias;
    private int maxIterations;
    private double learningRate;

    public Perceptron(int maxIterations, double learningRate) {
        this.maxIterations = maxIterations;
        this.learningRate = learningRate;
    }
    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }
    @Override
    public void train(List<Instance<Double, Integer>> instances)
    {
        int numFeatures = instances.get(0).getInput().size();
        weights = new double[numFeatures];
        bias = 0.0;

        for(int iteration = 0; iteration < maxIterations; iteration++)
        {
            for(Instance<Double, Integer> instance: instances)
            {
                List<Double> input = instance.getInput();
                int label = (int) instance.getOutput();
                double predicition = predictRaw(input);

                if(label * predicition <= 0)
                {
                    for(int i = 0; i < weights.length;i++)
                    {
                        weights[i] += learningRate * label * ((Number) input.get(i)).doubleValue();
                    }
                    bias += learningRate*label;
                }

            }
        }
    }
    @Override
    public List<Integer> test(List<Instance<Double, Integer>> instances)
    {
        return (List<Integer>) instances.stream()
                .map(instance -> predict(instance.getInput()))
                .toList();
    }
    private double predictRaw(List<Double> input) {
        double sum = 0.0;
        for (int i = 0; i < input.size(); i++) {
            sum += weights[i] * ((Number) input.get(i)).doubleValue();
        }
        return sum + bias;
    }

    public int predict(List<Double> input) {
        return predictRaw(input) >= 0 ? 1 : -1;
    }

}
