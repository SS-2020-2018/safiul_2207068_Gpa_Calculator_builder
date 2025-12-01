package org.example.project1;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    @FXML
    private TextField courseNameField;
    @FXML
    private TextField courseCreditField;
    @FXML
    private TextField rollField;
    @FXML
    private TextField teacher1Field;
    @FXML
    private TextField teacher2Field;
    @FXML
    private ChoiceBox<String> gradeChoiceBox;
    @FXML
    private ListView<String> sampleListView;
    @FXML
    private Label cgpaLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button searchbutton;

    private ObservableList<String> observableRecords = FXCollections.observableArrayList();
    private DatabaseHelper dbHelper = new DatabaseHelper();

    @FXML
    private void initialize() {
        if (gradeChoiceBox != null && gradeChoiceBox.getItems().isEmpty()) {
            gradeChoiceBox.getItems().addAll("A+","A","A-","B+","B","B-","C","F");
            gradeChoiceBox.setValue("A");
        }
        if (sampleListView != null) {
            sampleListView.setItems(observableRecords);
            loadAllData();
        }
    }

    private void loadRollData(String roll) {
        new Thread(() -> {
            ObservableList<String> list = dbHelper.getRecordsByRoll(roll);
            Platform.runLater(() -> observableRecords.setAll(list));
        }).start();
    }

    private void loadAllData() {
        new Thread(() -> {
            ObservableList<String> list = dbHelper.getAllRecords();
            Platform.runLater(() -> observableRecords.setAll(list));
        }).start();
    }

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

    public void switchToScene3(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("scene3.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onSaveClicked(ActionEvent event) {
        String name = courseNameField.getText();
        String credit = courseCreditField.getText();
        String roll = rollField.getText();
        String t1 = teacher1Field.getText();
        String t2 = teacher2Field.getText();
        String grade = gradeChoiceBox.getValue();
        if (roll == null || roll.isEmpty()) return;
        if ((name == null || name.isEmpty()) && (credit == null || credit.isEmpty())) return;
        dbHelper.insertRecord(name, credit, roll, t1, t2, grade);
        loadRollData(roll);
        clearCourseInputs();
    }

    private void clearCourseInputs() {
        courseNameField.clear();
        courseCreditField.clear();
        teacher1Field.clear();
        teacher2Field.clear();
        gradeChoiceBox.setValue("A");
    }

    @FXML
    private void onDeleteClicked(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Records");
        dialog.setHeaderText("Delete all records for a roll");
        dialog.setContentText("Enter roll:");
        String roll = dialog.showAndWait().orElse("");
        if (roll == null || roll.isEmpty()) return;
        dbHelper.deleteByRoll(roll);
        observableRecords.clear();
        cgpaLabel.setText("0.00");
    }

    @FXML
    private void onsearchClicked(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Records");
        dialog.setHeaderText("Search records by roll");
        dialog.setContentText("Enter roll:");
        String roll = dialog.showAndWait().orElse("");
        if (roll == null || roll.isEmpty()) return;
        new Thread(() -> {
            ObservableList<String> results = dbHelper.searchRecordsByRoll(roll);
            Platform.runLater(() -> observableRecords.setAll(results));
        }).start();
    }

    @FXML
    private void onRefreshClicked(ActionEvent event) {
        String roll = rollField.getText();
        observableRecords.clear();
        cgpaLabel.setText("0.00");
        clearCourseInputs();
        if (roll != null && !roll.isEmpty()) {
            loadRollData(roll);
        }
    }

    @FXML
    private void onCalculateClicked(ActionEvent event) {
        String roll = rollField.getText();
        if (roll == null || roll.isEmpty()) return;
        double cgpa = dbHelper.calculateCgpaForRoll(roll);
        cgpaLabel.setText(String.format("%.2f", cgpa));
    }

    @FXML
    private void onCalculateScene3(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Calculate CGPA");
        dialog.setHeaderText("Enter roll to calculate CGPA");
        dialog.setContentText("Roll:");
        String roll = dialog.showAndWait().orElse("");
        if (roll == null || roll.isEmpty()) return;
        loadRollData(roll);
        double cgpa = dbHelper.calculateCgpaForRoll(roll);
        if (cgpaLabel != null) {
            cgpaLabel.setText(String.format("%.2f", cgpa));
        }
    }

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
