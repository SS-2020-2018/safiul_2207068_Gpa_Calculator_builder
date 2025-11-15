package com.example.pro_1_gpa_calc;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {


    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToScene1(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Scene1.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToScene2(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Scene2.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private ChoiceBox<String> mychoicebox=new ChoiceBox<>();

    public void initialize(URL location, ResourceBundle resources)
    {
        mychoicebox.getItems().addAll("A+","A","A-","B+","B","B-","C","F");
    }
   @FXML
    private VBox courseVBox;

    @FXML
    private void handleNewButton(ActionEvent event) {
        HBox courseRow = new HBox(15);
        TextField name = new TextField();
        name.setPromptText("Course Name");
        TextField code = new TextField();
        code.setPromptText("Course Code");
        TextField credit=new TextField();
        credit.setPromptText("Credit");
        TextField t1=new TextField();
        t1.setPromptText("Teacher 1 name");
        TextField t2=new TextField();
        t2.setPromptText("Teacher 2 name");
        ChoiceBox<String>choiceBox=new ChoiceBox<>();
        choiceBox.getItems().addAll("A+","A","A-","B+","B","B-","C","F");
        courseRow.getChildren().addAll(name, code ,credit,t1,t2,choiceBox);
        courseVBox.getChildren().add(courseRow);
        VBox.setMargin(courseRow, new Insets(10, 0, 10, 0));
    }

    @FXML
    private Button addButton;

    @FXML
    private Button doneButton;
    private void handleDoneButton(ActionEvent event) {
        addButton.setDisable(true);
        doneButton.setDisable(true);
        for (Node node : courseVBox.getChildren()) {
            if (node instanceof HBox) {
                HBox row = (HBox) node;
                row.getChildren().forEach(child -> child.setDisable(true));
            }
        }
    }

}