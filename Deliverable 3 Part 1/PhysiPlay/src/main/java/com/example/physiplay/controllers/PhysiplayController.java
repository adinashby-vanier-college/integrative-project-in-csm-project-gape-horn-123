package com.example.physiplay.controllers;

import com.example.physiplay.SimulationObject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Objects;
import java.util.Optional;

public class PhysiplayController {

    Stage mainWindow;
    ArrayList<SimulationObject> presetList = new ArrayList<>();

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

    public PhysiplayController(Stage stage){
        this.mainWindow = stage;
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
    public void initialize() {
        createPresetButton.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/createPreset.fxml"));
            Stage presetWindow = new Stage();
            CreatePresetController createPresetController = new CreatePresetController(presetWindow, presetHBox, presetList);
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
        });

        closeMenuItem.setOnAction(event -> {
            displayClosingAlertBox();
        });

        TreeItem<String> rootItem = new TreeItem<>("SampleScene");
        rootItem.setExpanded(true);

        for (int i = 1; i < 6; i++) {
            TreeItem<String> item = new TreeItem<>("GameObject " + i);
            rootItem.getChildren().add(item);
        }

        hierarchyView.setRoot(rootItem);
    }
}
