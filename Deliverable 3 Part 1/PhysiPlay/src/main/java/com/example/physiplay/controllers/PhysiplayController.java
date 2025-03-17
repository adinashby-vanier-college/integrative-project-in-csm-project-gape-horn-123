package com.example.physiplay.controllers;

import com.example.physiplay.SimulationObject;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PhysiplayController {

    Stage mainWindow;
    ArrayList<SimulationObject> presetList = new ArrayList<>();
    ArrayList<SimulationObject> objectsList = new ArrayList<>();

    @FXML
    Button createPresetButton;
    @FXML
    Button startButton;
    @FXML
    HBox presetHBox;
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private TreeView<String> hierarchyView;
    @FXML
    FlowPane presetFlowPane;
    @FXML
    ScrollPane presetScrollPane;
    @FXML
    Canvas canvas;

    public PhysiplayController(Stage stage){
        this.mainWindow = stage;
    }

    public void initialize() {
        presetScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        presetScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        presetFlowPane.setHgap(20);
        presetFlowPane.setVgap(20);

        createPresetButton.setOnAction(event -> createPresetWindow());

        closeMenuItem.setOnAction(event ->
            displayClosingAlertBox()
        );

        TreeItem<String> rootItem = new TreeItem<>("SampleScene");
        rootItem.setExpanded(true);
        hierarchyView.setRoot(rootItem);

        //setUpTimer();
    }

    private void setUpTimer() {
        AnimationTimer animationTimer = new AnimationTimer() {
            public void handle(long l) {
                ObservableList<Node> vBoxList = presetFlowPane.getChildren();
                if (!vBoxList.isEmpty())System.out.println(vBoxList.get(0));
            }
        };
        animationTimer.start();
    }

    private void displayClosingAlertBox() {
        Alert closingAlert = new Alert(Alert.AlertType.CONFIRMATION);
        closingAlert.setTitle("Exiting");
        closingAlert.setHeaderText("Are you sure you want to exit?");
        Optional<ButtonType> result = closingAlert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            Platform.exit();
        }
        else closingAlert.close();
    }

    public void createPresetWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/createPreset.fxml"));
        Stage presetWindow = new Stage();
        CreatePresetController createPresetController = new CreatePresetController(presetWindow, presetHBox, presetList, presetFlowPane, hierarchyView, canvas.getGraphicsContext2D(), objectsList);
        loader.setController(createPresetController);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root, 500, 900);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fonts/stylesheetPresetWindow.css")).toExternalForm());
        presetWindow.setScene(scene);
        presetWindow.initModality(Modality.WINDOW_MODAL);
        presetWindow.initOwner(mainWindow);
        presetWindow.show();
    }
}
