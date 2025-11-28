package org.example.project1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloNEWController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private VBox courseVBox;
    @FXML
    private Label gpaLabel;
    public void switchToScene1(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("scene1.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToScene2(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("scene1_1.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private TextField courseNameField;

    @FXML
    private TextField courseCreditField;

    @FXML
    private TextField teacher1Field;

    @FXML
    private TextField teacher2Field;

    @FXML
    private ChoiceBox<String> gradeChoiceBox;

    // Left-side list of records
    @FXML
    private ListView<String> sampleListView;

    // Bottom CGPA label
    @FXML
    private Label cgpaLabel;

    // Called automatically after FXML is loaded
    @FXML
    private void initialize() {
        if (gradeChoiceBox != null && gradeChoiceBox.getItems().isEmpty()) {
            gradeChoiceBox.getItems().addAll("A+","A","A-","B+","B","B-","C","F");
            gradeChoiceBox.setValue("A");
        }
    }

    // Save button: store current form values into ListView
    @FXML
    private void onSaveClicked(ActionEvent event) {
        if (sampleListView == null) return;

        String name   = courseNameField.getText();
        String credit = courseCreditField.getText();
        String t1     = teacher1Field.getText();
        String t2     = teacher2Field.getText();
        String grade  = gradeChoiceBox.getValue();

        // Skip empty record
        if ((name == null || name.isEmpty()) &&
                (credit == null || credit.isEmpty())) {
            return;
        }

        String line = String.format(
                "%s, Cr: %s, T1: %s, T2: %s, Grade: %s",
                name, credit, t1, t2, grade
        );
        sampleListView.getItems().add(line);

        // Optionally clear inputs after save
        courseNameField.clear();
        courseCreditField.clear();
        teacher1Field.clear();
        teacher2Field.clear();
        gradeChoiceBox.setValue("A");
    }

    // Calculate button: compute CGPA from items in ListView
    @FXML
    private void onCalculateClicked(ActionEvent event) {
        if (sampleListView == null || cgpaLabel == null) return;

        double totalPoints = 0.0;
        double totalCredits = 0.0;

        for (String item : sampleListView.getItems()) {
            // item format:
            // "<name>, Cr: <credit>, T1: ..., T2: ..., Grade: <grade>"
            // very simple parsing by splitting
            try {
                String[] parts = item.split(",");
                // parts[1] like " Cr: 3"
                String crPart = parts[1].trim();      // "Cr: 3"
                String[] crSplit = crPart.split(":");
                double credit = Double.parseDouble(crSplit[1].trim());

                // last part like " Grade: A"
                String gradePart = parts[parts.length - 1].trim(); // "Grade: A"
                String[] gSplit = gradePart.split(":");
                String grade = gSplit[1].trim();

                double gp = gradeToPoint(grade);
                totalCredits += credit;
                totalPoints  += gp * credit;
            } catch (Exception e) {
                // ignore badly formatted items
            }
        }

        double cgpa = 0.0;
        if (totalCredits > 0) {
            cgpa = totalPoints / totalCredits;
        }
        cgpaLabel.setText(String.format("%.2f", cgpa));
    }

    // Optional: not used yet
    @FXML
    private void onUpdateClicked(ActionEvent event) {
        // You can implement editing of selected ListView item if needed
    }

    @FXML
    private void onDeleteClicked(ActionEvent event) {
        if (sampleListView != null) {
            int index = sampleListView.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                sampleListView.getItems().remove(index);
            }
        }
    }

    // Convert letter grade to grade point
    private double gradeToPoint(String grade) {
        if (grade == null) return 0.0;
        return switch (grade) {
            case "A+" -> 4.0;
            case "A"  -> 3.75;
            case "A-" -> 3.5;
            case "B+" -> 3.25;
            case "B"  -> 3.0;
            case "B-" -> 2.75;
            case "C"  -> 2.5;
            case "F"  -> 0.0;
            default   -> 0.0;
        };
    }
}
