package Model;

import Domain.Instance;
import Domain.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//Naive Bayes is a probabilistic classifier based on Bayes' theorem with the assumption of independence between features.
// It is commonly used for classification tasks. The key idea is to predict the class of a new instance based on the probabilities of the features given each class.
// The steps involved in Naive Bayes are:
//Calculate Prior Probabilities: Compute the prior probability of each class based on the training data.
//Calculate Likelihoods: Compute the likelihood of each feature given each class.
//Apply Bayes' Theorem: Use Bayes' theorem to compute the posterior probability of each class given the features of the new instance.
//Make a Prediction: Predict the class with the highest posterior probability.
public class NaiveBayes implements Model<Double, Integer> {
    private Map<Integer, Double> classProbabilities; // P(Class)
    private Map<Integer, Map<Integer, Double>> featureProbabilities; // P(Feature | Class)

    public NaiveBayes() {
        classProbabilities = new HashMap<>();
        featureProbabilities = new HashMap<>();
    }

    @Override
    public void train(List<Instance<Double, Integer>> instances) {
        int numFeatures = instances.get(0).getInput().size(); // Assume all instances have the same number of features

        // Count occurrences for classes and features
        Map<Integer, Integer> classCounts = new HashMap<>();
        Map<Integer, Map<Integer, Double>> featureSums = new HashMap<>();

        for (Instance<Double, Integer> instance : instances) {
            Integer output = instance.getOutput();
            classCounts.put(output, classCounts.getOrDefault(output, 0) + 1);

            // Initialize the inner map for features if it doesn't exist
            featureSums.putIfAbsent(output, new HashMap<>());

            for (int i = 0; i < numFeatures; i++) {
                double featureValue = instance.getInput().get(i);
                featureSums.get(output).put(i, featureSums.get(output).getOrDefault(i, 0.0) + featureValue);
            }
        }

        // Calculate probabilities
        int totalInstances = instances.size();

        // Class probabilities P(Class)
        for (Map.Entry<Integer, Integer> entry : classCounts.entrySet()) {
            classProbabilities.put(entry.getKey(), entry.getValue() / (double) totalInstances);
        }

        // Feature probabilities P(Feature | Class)
        for (Map.Entry<Integer, Map<Integer, Double>> entry : featureSums.entrySet()) {
            Integer output = entry.getKey();
            Map<Integer, Double> featureSumsForClass = entry.getValue();

            featureProbabilities.putIfAbsent(output, new HashMap<>());
            int classCount = classCounts.get(output);

            for (Map.Entry<Integer, Double> featureEntry : featureSumsForClass.entrySet()) {
                int featureIndex = featureEntry.getKey();
                double sum = featureEntry.getValue();
                featureProbabilities.get(output).put(featureIndex, sum / classCount); // Mean value for feature
            }
        }
    }

    @Override
    public List<Integer> test(List<Instance<Double, Integer>> instances) {
        return instances.stream()
                .map(this::predict)
                .toList();
    }

    // Predicts the class for a single instance
    private Integer predict(Instance<Double, Integer> instance) {
        double maxProbability = Double.NEGATIVE_INFINITY;
        Integer bestClass = null;

        for (Integer outputClass : classProbabilities.keySet()) {
            double classProbability = Math.log(classProbabilities.get(outputClass)); // Use log to avoid underflow
            for (int i = 0; i < instance.getInput().size(); i++) {
                double featureValue = instance.getInput().get(i);
                double featureMean = featureProbabilities.get(outputClass).getOrDefault(i, 0.0);

                // Using a simple Gaussian assumption for P(Feature | Class)
                double likelihood = Math.exp(-Math.pow(featureValue - featureMean, 2) / 2) / Math.sqrt(2 * Math.PI);
                classProbability += Math.log(likelihood + 1e-6); // Add a small value to avoid log(0)
            }

            if (classProbability > maxProbability) {
                maxProbability = classProbability;
                bestClass = outputClass;
            }
        }

        return bestClass;
    }
}
