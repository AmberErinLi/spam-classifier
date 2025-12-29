import java.io.*;
import java.util.*;

// This class uses a classification tree to predict a label for some given text.
public class Classifier {
    private ClassifierNode overallRoot;

    // Behavior: 
    //   - Creates a classification tree using the contents from the given Scanner. Contents
    //     are loaded in a pre-order format.
    // Exceptions:
    //   - Throws an IllegalArgumentException if the given input is null
    // Parameters:
    //   - Scanner input - where the contents of the classification tree will be loaded from
    public Classifier(Scanner input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null.");
        }
        overallRoot = constructTree(input);
    }

    // Behavior: 
    //   - Creates a classification tree using the contents from the given Scanner. Contents
    //     are loaded in a pre-order format.
    // Returns:
    //   - ClassifierNode - the new node that represents this classification tree
    // Parameters:
    //   - Scanner input - where the contents of the classification tree will be loaded from
    private ClassifierNode constructTree(Scanner input) {
        ClassifierNode newNode = null;
        if (input.hasNextLine()) {
            String line = input.nextLine();
            if (line.contains("Feature")) {
                String feature = line.substring("Feature: ".length());
                double threshold = Double.parseDouble(input.nextLine().substring
                    ("Threshold: ".length()));
                newNode = new ClassifierNode(feature, threshold,
                    constructTree(input), constructTree(input));
            } else {
                String label = line;
                newNode = new ClassifierNode(label);
            }
        }
        return newNode;
    }

    // Behavior:
    //   - Creates a classification tree from the text inputs and corresponding labels. Uses 
    //     this information to train the tree to produce more accurate results. 
    // Exceptions:
    //   - Throws an IllegalArgumentException if the given 'textBlocks' or 'labels' is null,
    //     if either of those two lists are empty, or if they are different lengths.
    // Parameters:
    //   - List<TextBlock> textBlocks - the list of text inputs that will be used to build and
    //     train the tree 
    //   - List<String> labels - the list of corresponding labels that are the expected output
    //     for each text block
    public Classifier(List<TextBlock> textBlocks, List<String> labels) {
        if (textBlocks == null || labels == null || textBlocks.size() != labels.size() || 
            textBlocks.size() == 0 || labels.size() == 0) {
            throw new IllegalArgumentException();
        }
        overallRoot = new ClassifierNode(labels.get(0), textBlocks.get(0));
        for (int i = 1; i < labels.size(); i++) {
            overallRoot = constructTree(textBlocks.get(i), labels.get(i), overallRoot);  
        }  
    }

    // Behavior:
    //   - Creates a classification tree from the text inputs and corresponding labels. Uses 
    //     this information to train the tree to produce more accurate results.
    // Returns:
    //   - ClassifierNode - the new node that represents this classification tree
    // Parameters:
    //   - List<TextBlock> textBlocks - the list of text inputs that will be used to build and
    //     train the tree 
    //   - List<String> labels - the list of corresponding labels that are the expected output
    //     for each text block
    //   - ClassifierNode root - the current node being checked if it has an accurate label
    private ClassifierNode constructTree(TextBlock textBlock, String label, ClassifierNode root) {
        if (root.label != null && !root.label.equals(label)) {
            String feature = root.data.findBiggestDifference(textBlock);
            double rootProbability = root.data.get(feature);
            double currProbability = textBlock.get(feature);
            double threshold = midpoint(rootProbability, currProbability);
            ClassifierNode left = null;
            ClassifierNode right = null;
            if (rootProbability < threshold) {
                left = root;
                right = new ClassifierNode(label, textBlock);
            } else {
                left = new ClassifierNode(label, textBlock);
                right = root;
            }
            ClassifierNode decisionNode = new ClassifierNode(feature, threshold, left, right);
            return decisionNode;
        } else if (root.label == null) {
            if (textBlock.get(root.feature) < root.threshold) {
                root.left = constructTree(textBlock, label, root.left);
            } else {
                root.right = constructTree(textBlock, label, root.right);
            }
        }
        return root;
    }

    // Behavior:
    //   - Returns the label for the given input that is predicted by this classification
    //     tree
    // Exceptions:
    //   - Throws an IllegalArgumentException if the given text input is null
    // Parameters:
    //   - TextBlock input - the text input that the classifier will predict a label for
    // Returns:
    //   - The predicted label
    public String classify(TextBlock input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null.");
        }
        return classify(input, overallRoot);
    }

    // Behavior:
    //   - Returns the label for the given input that is predicted by this classification
    //     tree.
    // Parameters:
    //   - TextBlock input - the text input that the classifier will predict a label for
    //   - ClassifierNode curr - the current node that is being compared with the input to 
    //     make progress towards assigning the input a label
    // Returns:
    //   - The predicted label
    private String classify(TextBlock input, ClassifierNode curr) {
        if (curr.feature != null) {
            if (input.get(curr.feature) < curr.threshold) {
                return classify(input, curr.left);
            } else {
                return classify(input, curr.right);
            }
        } else {
            return curr.label;
        }       
    }

    // Behavior:
    //   - Saves this classification tree to the given output in a pre-order format
    //   - Nodes that contain a feature will be saved with one line starting with
    //     "Feature: ", followed by the feature, and another line starting with
    //     "Threshold: ", followed by the threshold.
    //   - Nodes that contain a label will be saved on one line as just the label.
    // Exceptions:
    //   - Throws an IllegalArgumentException if the given output is null
    // Parameters:
    //   - PrintStream output - The output destination where the classification tree's
    //     data is saved
    public void save(PrintStream output) {
        if (output == null) {
            throw new IllegalArgumentException("Output cannot be null.");
        }
        save(output, overallRoot);
    }

    // Behavior:
    //   - Saves this classification tree to the given output in a pre-order format
    //   - Nodes that contain a feature will be saved with one line starting with
    //     "Feature: ", followed by the feature, and another line starting with
    //     "Threshold: ", followed by the threshold.
    //   - Nodes that contain a label will be saved on one line as just the label.
    // Exceptions:
    //   - Throws an IllegalArgumentException if the given output is null
    // Parameters:
    //   - PrintStream output - The output destination where the classification tree's
    //     data is saved
    //   - ClassifierNode curr - The current node whose data is being printed to the
    //     output
    private void save(PrintStream output, ClassifierNode curr) {
        if (curr != null) {
            if (curr.feature != null) {
                output.println("Feature: " + curr.feature);
                output.println("Threshold: " + curr.threshold);
                save(output, curr.left);
                save(output, curr.right);
            } else {
                output.println(curr.label);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////
    // PROVIDED METHODS - **DO NOT MODIFY ANYTHING BELOW THIS LINE!** //
    ////////////////////////////////////////////////////////////////////

    // Helper method to calcualte the midpoint of two provided doubles.
    private static double midpoint(double one, double two) {
        return Math.min(one, two) + (Math.abs(one - two) / 2.0);
    }    

    // Behavior: Calculates the accuracy of this model on provided Lists of 
    //           testing 'data' and corresponding 'labels'. The label for a 
    //           datapoint at an index within 'data' should be found at the 
    //           same index within 'labels'.
    // Exceptions: IllegalArgumentException if the number of datapoints doesn't match the number 
    //             of provided labels
    // Returns: a map storing the classification accuracy for each of the encountered labels when
    //          classifying
    // Parameters: data - the list of TextBlock objects to classify. Should be non-null.
    //             labels - the list of expected labels for each TextBlock object. 
    //             Should be non-null.
    public Map<String, Double> calculateAccuracy(List<TextBlock> data, List<String> labels) {
        // Check to make sure the lists have the same size (each datapoint has an expected label)
        if (data.size() != labels.size()) {
            throw new IllegalArgumentException(
                    String.format("Length of provided data [%d] doesn't match provided labels " +
                                  "[%d]", data.size(), labels.size()));
        }
        
        // Create our total and correct maps for average calculation
        Map<String, Integer> labelToTotal = new HashMap<>();
        Map<String, Double> labelToCorrect = new HashMap<>();
        labelToTotal.put("Overall", 0);
        labelToCorrect.put("Overall", 0.0);
        
        for (int i = 0; i < data.size(); i++) {
            String result = classify(data.get(i));
            String label = labels.get(i);

            // Increment totals depending on resultant label
            labelToTotal.put(label, labelToTotal.getOrDefault(label, 0) + 1);
            labelToTotal.put("Overall", labelToTotal.get("Overall") + 1);
            if (result.equals(label)) {
                labelToCorrect.put(result, labelToCorrect.getOrDefault(result, 0.0) + 1);
                labelToCorrect.put("Overall", labelToCorrect.get("Overall") + 1);
            }
        }

        // Turn totals into accuracy percentage
        for (String label : labelToCorrect.keySet()) {
            labelToCorrect.put(label, labelToCorrect.get(label) / labelToTotal.get(label));
        }
        return labelToCorrect;
    }

    // This class represents a specific node within a classification tree.
    private static class ClassifierNode {
        public final String feature;
        public final double threshold;
        public final String label;
        public final TextBlock data;
        public ClassifierNode left;
        public ClassifierNode right;

        // Behavior:
        //   - Creates a new ClassifierNode with the given label
        // Parameters:
        //   - String label - the label for this new ClassifierNode
        public ClassifierNode(String label) {
            this(label, null);
        }
        
        // Behavior:
        //   - Creates a new ClassifierNode with the TextBlock data and corresponding label
        // Parameters:
        //   - String label - the label for this new ClassifierNode
        //   - TextBlock data - the TextBlock datapoint that corresponds to the label
        public ClassifierNode(String label, TextBlock data) {
            this.label = label;
            this.data = data;
            this.feature = null;
            this.threshold = 0.0;
            this.left = null;
            this.right = null;
        }

        // Behavior:
        //   - Creates a new ClassifierNode with the given feature and threshold and left
        //     and right child nodes 
        // Parameters:
        //   - String feature - the feature of this ClassifierNode
        //   - double threshold - the threshold that organizes where a TextBlock should go 
        //     in the classification tree
        //   - ClassifierNode left - this ClassifierNode's left child
        //   - ClassifierNode right - this ClassifierNode's right child
        public ClassifierNode(String feature, double threshold, ClassifierNode left, 
                              ClassifierNode right) {
            this.feature = feature;
            this.threshold = threshold;
            this.label = null;
            this.data = null;
            this.left = left;
            this.right = right;
        }
    }
}
