package Evaluation;

import Domain.Instance;

import java.util.List;

@FunctionalInterface
public interface EvaluationMeasure<F,L> {
    public double evaluate(List<Instance<F,L>> instances, List<L> predictions);

}
