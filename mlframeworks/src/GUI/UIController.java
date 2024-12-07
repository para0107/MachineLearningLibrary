package GUI;

import Model.KnearestNeighbors;
import Model.NaiveBayes;
import Model.Perceptron;
import Domain.Instance;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import persistence.DataLoader;

import java.io.File;
import java.util.List;

public class UIController {

    @FXML
    private Button chooseFileButton;

    @FXML
    private Label fileLabel;

    @FXML
    private ComboBox<String> modelComboBox;

    @FXML
    private VBox parameterBox;

    @FXML
    private Slider trainTestSlider;

    @FXML
    private Label splitLabel;

    @FXML
    private Button trainButton;

    @FXML
    private Button testButton;

    @FXML
    private TextArea outputArea;

    @FXML
    private TextArea accuracyTextArea;

    @FXML
    private TextField numIterationsTextField;  // For specifying number of iterations

    @FXML
    private TextField learningRateTextField;   // For Perceptron learning rate

    @FXML
    private TextField kValueTextField;          // For KNN k value

    private File selectedFile;
    private List<Instance<Double, Integer>> trainingData;
    private List<Instance<Double, Integer>> testData;

    // Model instances
    private KnearestNeighbors<Double, Integer> knnModel;
    private NaiveBayes naiveBayesModel;
    private Perceptron perceptronModel;

    @FXML
    public void initialize() {
        // Initialize models and combo box
        modelComboBox.getItems().addAll("KNearestNeighbour", "Perceptron", "Naive Bayes");

        // Set initial label for train-test split
        updateSplitLabel();

        // Load data
        loadData();

        // Set default values
        numIterationsTextField.setText("1000");  // Default 1000 iterations
        learningRateTextField.setText("0.01");   // Default learning rate for Perceptron
        kValueTextField.setText("5");             // Default k value for KNN
    }

    @FXML
    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Input File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            fileLabel.setText(selectedFile.getName());
        }
    }

    private void loadData() {
        if (selectedFile != null) {
            // Split the data into train and test sets based on slider value
            List<Instance<Double, Integer>> data = DataLoader.loadCSV(selectedFile);
            int trainSize = (int) (trainTestSlider.getValue() / 100 * data.size());
            trainingData = data.subList(0, trainSize);
            testData = data.subList(trainSize, data.size());
        }
    }

    @FXML
    public void trainModel() {
        if (selectedFile == null) {
            outputArea.appendText("Please select a file before training.\n");
            return;
        }

        String selectedModel = modelComboBox.getValue();
        if (selectedModel == null) {
            outputArea.appendText("Please select a model to train.\n");
            return;
        }

        loadData();

        // Get the number of iterations from the text field (default to 1000)
        int numIterations = 1000; // Default value
        try {
            numIterations = Integer.parseInt(numIterationsTextField.getText());
        } catch (NumberFormatException e) {
            outputArea.appendText("Invalid number of iterations. Defaulting to 1000.\n");
        }

        // Get the learning rate for Perceptron
        double learningRate = 0.01; // Default learning rate
        try {
            learningRate = Double.parseDouble(learningRateTextField.getText());
        } catch (NumberFormatException e) {
            outputArea.appendText("Invalid learning rate. Defaulting to 0.01.\n");
        }

        // Get k value for KNN
        int k = 5; // Default k value for KNN
        try {
            k = Integer.parseInt(kValueTextField.getText());
        } catch (NumberFormatException e) {
            outputArea.appendText("Invalid k value. Defaulting to 5.\n");
        }

        outputArea.appendText("Training model: " + selectedModel + " for " + numIterations + " iterations.\n");

        switch (selectedModel) {
            case "KNearestNeighbour":
                knnModel = new KnearestNeighbors<Double,Integer>(k);
                while(numIterations > 0){
                knnModel.train(trainingData);
                numIterations--;}
                // KNN doesn't need iterations, train once
                break;
            case "Perceptron":
                perceptronModel = new Perceptron(numIterations, learningRate);
                while(numIterations > 0){
                    perceptronModel.train(trainingData);
                    numIterations--;}// Use iterations and learning rate
                //perceptronModel.train(trainingData);
                break;
            case "Naive Bayes":
                naiveBayesModel = new NaiveBayes();
                while(numIterations > 0){
                    naiveBayesModel.train(trainingData);
                    numIterations--;}
               // naiveBayesModel.train(trainingData);  // Naive Bayes doesn't need iterations or parameters
                break;
            default:
                outputArea.appendText("Unknown model selected.\n");
        }
        outputArea.appendText("Training complete.\n");
    }

    @FXML
    public void testModel() {
        if (selectedFile == null) {
            outputArea.appendText("Please select a file before testing.\n");
            return;
        }

        String selectedModel = modelComboBox.getValue();
        if (selectedModel == null) {
            outputArea.appendText("Please select a model to test.\n");
            return;
        }

        outputArea.appendText("Testing model: " + selectedModel + "\n");

        switch (selectedModel) {
            case "KNearestNeighbour":
                List<Integer> knnPredictions = knnModel.test(testData);
                outputResults(knnPredictions);
                break;
            case "Perceptron":
                List<Integer> perceptronPredictions = perceptronModel.test(testData);
                outputResults(perceptronPredictions);
                break;
            case "Naive Bayes":
                List<Integer> naiveBayesPredictions = naiveBayesModel.test(testData);
                outputResults(naiveBayesPredictions);
                break;
            default:
                outputArea.appendText("Unknown model selected.\n");
        }
    }

    private void outputResults(List<Integer> predictions) {
        // Calculate accuracy
        int correct = 0;
        for (int i = 0; i < testData.size(); i++) {
            if (predictions.get(i).equals(testData.get(i).getOutput())) {
                correct++;
            }
        }
        double accuracy = (double) correct / testData.size() * 100;
        accuracyTextArea.setText("Accuracy: " + accuracy + "%\n");

        // Print confusion matrix (for simplicity, binary classification example)
        int tp = 0, fp = 0, tn = 0, fn = 0;
        for (int i = 0; i < testData.size(); i++) {
            int actual = testData.get(i).getOutput();
            int predicted = predictions.get(i);
            if (actual == 1 && predicted == 1) tp++;
            if (actual == 1 && predicted == -1) fn++;
            if (actual == -1 && predicted == 1) fp++;
            if (actual == -1 && predicted == -1) tn++;
        }
        outputArea.appendText("Confusion Matrix:\n");
        outputArea.appendText("[[" + tp + ", " + fp + "], [" + fn + ", " + tn + "]]\n");
    }

    @FXML
    private void updateSplitLabel() {
        int trainPercentage = (int) trainTestSlider.getValue();
        int testPercentage = 100 - trainPercentage;
        splitLabel.setText("Train: " + trainPercentage + "%, Test: " + testPercentage + "%");
    }

    // Method to dynamically update the parameter box based on the selected model
    @FXML
    public void updateParameterBox() {
        parameterBox.getChildren().clear(); // Clear existing inputs

        String selectedModel = modelComboBox.getValue();
        if (selectedModel != null) {
            switch (selectedModel) {
                case "KNearestNeighbour":
                    Label kLabel = new Label("Enter k value (number of neighbors): ");
                    parameterBox.getChildren().add(kLabel);
                    parameterBox.getChildren().add(kValueTextField);
                    break;
                case "Perceptron":
                    Label lrLabel = new Label("Enter Learning Rate: ");
                    Label iterLabel = new Label("Enter Number of Iterations: ");
                    parameterBox.getChildren().add(lrLabel);
                    parameterBox.getChildren().add(learningRateTextField);
                    parameterBox.getChildren().add(iterLabel);
                    parameterBox.getChildren().add(numIterationsTextField);
                    break;
                case "Naive Bayes":
                    // No specific parameters for Naive Bayes in this example
                    break;
                default:
                    break;
            }
        }
    }
}
