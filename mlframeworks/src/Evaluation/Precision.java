package Evaluation;

import Domain.Instance;

import java.util.List;

public class Precision<F, L> implements EvaluationMeasure<F, L> {
    @Override
    public double evaluate(List<Instance<F, L>> instances, List<L> predictions) {
        int truePositive = 0;
        int falsePositive = 0;

        for (int i = 0; i < instances.size(); i++) {
            L actual = instances.get(i).getOutput();
            L predicted = predictions.get(i);

            if (predicted.equals(actual)) {
                if (predicted.equals(1)) truePositive++;
            } else {
                if (predicted.equals(1)) falsePositive++;
            }
        }

        return truePositive / (double) (truePositive + falsePositive);
    }
}
