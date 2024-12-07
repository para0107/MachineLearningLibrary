package Model;

public class ModelTrainer {
    private final String filePath;
    private final String modelName;
    private final int trainPercentage;

    private double accuracy;

    public ModelTrainer(String filePath, String modelName, int trainPercentage) {
        this.filePath = filePath;
        this.modelName = modelName;
        this.trainPercentage = trainPercentage;
    }

    public void train() {
        // Simulate training logic
        System.out.println("Training " + modelName + " with data from: " + filePath);
        this.accuracy = Math.random() * 0.2 + 0.8; // Random accuracy between 80-100%
    }

    public double getAccuracy() {
        return accuracy;
    }

    public String saveModel() {
        // Simulate model persistence
        return "models/" + modelName.toLowerCase().replace(" ", "_") + ".bin";
    }
}

