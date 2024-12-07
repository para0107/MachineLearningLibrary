package Model;
public class ModelTester {
    private final String filePath;
    private final String modelName;

    public ModelTester(String filePath, String modelName) {
        this.filePath = filePath;
        this.modelName = modelName;
    }

    public double test() {
        // Simulate testing logic
        System.out.println("Testing " + modelName + " with data from: " + filePath);
        return Math.random() * 0.2 + 0.8; // Random accuracy between 80-100%
    }
}

