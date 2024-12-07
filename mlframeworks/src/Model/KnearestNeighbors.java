package Model;

import Domain.Instance;
import Domain.Model;

import java.util.*;
//K-Nearest Neighbors (KNN) Concept
////K-Nearest Neighbors (KNN) is a simple, non-parametric, and lazy learning algorithm used for classification and regression.
// The key idea is to predict the label of a new instance based on the labels of its k nearest neighbors in the training data.
// The steps involved in KNN are:
////Choose the number of neighbors k: This is a hyperparameter that determines how many neighbors will be used to make the prediction.
////Calculate the distance: Compute the distance between the new instance and all instances in the training data. Common distance metrics include Euclidean, Manhattan, and Minkowski distances.
////Find the nearest neighbors: Identify the k instances in the training data that are closest to the new instance.
////Make a prediction: For classification, the prediction is typically the mode (most common label) of the k nearest neighbors. For regression, the prediction is usually the mean of the k nearest neighbors' values.
public class KnearestNeighbors<F,L> implements Model<F,L> {
    private List<Instance<F,L>> trainingData;
    private int k;

    public KnearestNeighbors(int k){
        this.k = k;
    }

    public void train(List<Instance<F, L>> instances) {
        this.trainingData = instances;
    }
    @Override
    public List<L> test(List<Instance<F,L>> instances )
    {
        List<L> predictions = new ArrayList<>();
        for(Instance<F,L> instance : instances)
        {
            predictions.add(predict(instance));
        }
        return predictions;
    }
    private L predict(Instance<F,L> testInstance)
    {
        PriorityQueue<Neighbor> nearestNeighbors = new PriorityQueue<>(Comparator.comparingDouble(n->n.distance));
        for(Instance<F,L> trainingInstance : trainingData)
        {
            double distance = calculateDistance(testInstance.getInput(),trainingInstance.getInput());
            nearestNeighbors.add(new Neighbor(trainingInstance.getOutput(),distance));
        }
        Map<L,Integer> labelCounts = new HashMap<>();
        for(int i = 0; i< k && !nearestNeighbors.isEmpty(); i++)
        {
            L label = nearestNeighbors.poll().label;
            labelCounts.put(label, labelCounts.getOrDefault(label, 0)+1);
        }
        return labelCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }
    private double calculateDistance(List<F> input1, List<F> input2){
        double sum = 0.0;
        for(int i = 0; i < input1.size(); i++)
        {
            double value1 = ((Number) input1.get(i)).doubleValue();
            double value2 = ((Number) input2.get(i)).doubleValue();
            sum += Math.pow(value1 - value2, 2);
        }
        return Math.sqrt(sum);
    }
    private class Neighbor{
        L label;
        double distance;
        Neighbor(L label, double distance)
        {
            this.label = label;
            this.distance = distance;
        }
    }
    public void setK(int k) {
        this.k = k;
    }
}
