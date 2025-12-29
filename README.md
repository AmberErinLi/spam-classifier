# 📬 Spam Classifier — Machine Learning with Decision Trees

A Java-based **classification tree (decision tree)** that predicts whether a message is **Spam** or **Ham** based on text features.  
This project demonstrates foundations of **machine learning**, including training, prediction, evaluation, and model serialization — all implemented *from scratch* without ML libraries.

---

## 🚀 Overview

This project implements a simple machine-learning model: a **binary decision tree classifier**.  
It can be trained on labeled text data, classify new inputs, and save/load trained models to/from files.

The model works by:
- Analyzing text to extract **features** (word probabilities)
- Building a recursive **tree structure** of decision nodes and label nodes
- Comparing features against **thresholds** to choose a classification path
- Predicting a label based on where the input lands in the tree

While simplistic compared to modern ML techniques, this project reinforces core concepts of model structure, training logic, recursive algorithms, and the limits of ML prediction.

---

## 🧠 Machine Learning Concepts Used

| Term | Meaning in This Project |
|------|--------------------------|
| Model | The decision tree that classifies input text |
| Training | Building the tree structure using examples |
| Feature | Word probability values extracted from text |
| Label | Classification: `"Spam"` or `"Ham"` |
| Threshold | Numeric split values guiding tree decisions |

---

## 🧩 Project Structure

```text
spam-classifier/
├── src/
│ ├── Classifier.java # Builds, stores, trains, and evaluates the ML model
│ ├── ClassifierNode (inner) # Node structure for decisions + labels
│ ├── Client.java # Console UI to train, test, and classify input
│ ├── DataLoader.java # Loads CSV training/testing data
│ └── TextBlock.java # Provided text-featurization utility
│
├── data/
│ ├── emails/ # Sample spam/ham dataset
│ ├── federalist_papers/ # Alternative classification dataset
│ └── saved_models/ # Saved model files (.txt)
│
├── trees/ # Example trained models in save-file format
└── README.md
```

---

## 📊 Features

- Train classifier from labeled CSV data
- Predict labels on new text
- Calculate model accuracy
- Save/load trained models to human-readable `.txt` files
- Fully recursive tree construction
- No external ML libraries — **built from scratch**

---

## 🏗️ How It Works

### 🔧 Training
```java
Classifier classifier = new Classifier(trainData, trainLabels);
```
- Traverses the tree for each example
- If prediction is wrong → creates new decision node
- Uses findBiggestDifference() to select the best feature
- Uses midpoint between feature values as threshold

---

## 🔍 Classifying New Text
```java
String result = classifier.classify(inputTextBlock);
System.out.println(result); // "Spam" or "Ham"
```

---

## 💾 Saving / Loading Models
```java
classifier.save(new PrintStream("saved_models/myModel.txt"));
Classifier loaded = new Classifier(new Scanner(new File("saved_models/myModel.txt")));
```

---

## 📁 Example Model Output (save format)
A saved model file looks like:
```makefile
Feature: offer
Threshold: 0.267
Feature: click
Threshold: 0.105
Spam
Ham
```
This represents:
- If "offer" probability < 0.267 → go left
- Otherwise → go right
- Repeat until label is reached

---

## 🧪 Try It Yourself
Run the client program to train and evaluate:
```yaml
> java Client
Enter training file: data/emails/train.csv
Enter testing file: data/emails/test.csv

Training accuracy: 88.4%
Enter a file to classify: myEmail.csv
Prediction: Spam
```
(Exact accuracy will vary — the dataset is shuffled each run)

---

## 💡 Learning Outcomes
Through this project I practiced:

- Recursive algorithm design
- Binary tree construction
- Model evaluation & accuracy metrics
- Data parsing (CSV / text files)
- Program structure & documentation
- Interpreting model limitations

---

# 🎉 Thank you for checking out this project!
