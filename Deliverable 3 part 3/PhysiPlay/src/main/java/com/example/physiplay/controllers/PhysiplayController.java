package com.example.physiplay.controllers;

import com.example.physiplay.*;
import com.example.physiplay.components.*;
import com.example.physiplay.singletons.SettingsSingleton;
import com.example.physiplay.singletons.SimulationManager;
import com.example.physiplay.widgets.MyTreeCell;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.Joint;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class PhysiplayController {

    private final Scene scene;
    Stage mainWindow;
    ArrayList<SimulationObject> presetList = new ArrayList<>();
    ArrayList<SimulationObject> objectsList = new ArrayList<>();
    @FXML
    Button createPresetButton;
    @FXML
    Button startButton;
    @FXML
    Button pauseButton;
    @FXML
    HBox presetHBox;
    @FXML
    BorderPane borderPane;
    @FXML
    private TreeView<String> hierarchyView;
    @FXML
    FlowPane presetFlowPane;
    @FXML
    ScrollPane presetScrollPane;

    @FXML
    Canvas canvas;
    @FXML
    TabPane tabPane;

    @FXML
    Label labelPresets;
    @FXML
    Menu menuFile;
    @FXML
    Menu menuEdit;
    @FXML
    Menu menuView;
    @FXML
    Menu menuWindow;
    @FXML
    Menu menuHelp;

    @FXML
    MenuItem menuItemPositionCamReset;
    @FXML
    MenuItem menuItemScale;
    @FXML
    MenuItem menuItemNew;
    @FXML
    MenuItem menuItemOpen;
    @FXML
    MenuItem menuItemClear;
    @FXML
    MenuItem menuItemSettings;
    @FXML
    Menu menuCameraReset;
    @FXML
    CheckMenuItem checkMenuItemCanvasView;
    @FXML
    MenuItem menuItemClose;
    @FXML
    MenuItem menuItemHomeScreen;
    @FXML
    MenuItem menuItemPolygonVisualizer;
    @FXML
    MenuItem menuItemFullScreen;

    @FXML
    MenuItem saveMenuItem;
    @FXML
    MenuItem loadMenuItem;
    @FXML
    MenuItem menuItemAbout;

    RuntimeTypeAdapterFactory<Component> componentAdapter =
            RuntimeTypeAdapterFactory.of(Component.class, "type")
                    .registerSubtype(Rigidbody.class, "rigidbody")
                    .registerSubtype(RectangularRenderer.class, "rectangularRenderer")
                    .registerSubtype(CircleRenderer.class, "circleRenderer")
                    .registerSubtype(RegularPolygonRenderer.class, "regularPolygonRenderer")
                    .registerSubtype(PolygonRenderer.class, "polygonRenderer");

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Color.class, new ColorAdapter())
            .registerTypeAdapterFactory(componentAdapter)
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public PhysiplayController(Stage stage, Scene scene) {
        this.mainWindow = stage;
        this.scene = scene;
    }

    private void saveFile() {
        try (FileWriter writer = new FileWriter("myObj.data")) {
            System.out.println(gson.toJson(SimulationManager.getInstance().simulationObjectList));
            gson.toJson(SimulationManager.getInstance().simulationObjectList, writer);

        }
        catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);
            stage.toFront();
            alert.setTitle("Error");
            alert.setHeaderText("Something went wrong, please try again");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
    }

    private void destroyWorld() {
        Joint joint = SimulationManager.getInstance().world.getJointList();
        while (joint != null) {
            Joint next = joint.getNext();
            SimulationManager.getInstance().world.destroyJoint(joint);
            joint = next;
        }
        Body body = SimulationManager.getInstance().world.getBodyList();
        while (body != null) {
            Body next = body.getNext();
            SimulationManager.getInstance().world.destroyBody(next);
            body = next;
        }
    }
    private void loadFile() {
        Type listType = new TypeToken<List<SimulationObject>>(){}.getType();
        try (FileReader reader = new FileReader("myObj.data")) {
            List<SimulationObject> loadedObjects = gson.fromJson(reader, listType);
            // Clearing hierarchy inspector
            Body body = SimulationManager.getInstance().world.getBodyList();
            while (body != null) {
                Body next = body.getNext();
                SimulationManager.getInstance().world.destroyBody(body);
                body = next;
            }
            SimulationManager.getInstance().simulationObjectList.clear();
            SimulationManager.getInstance().dataMap.clear();
            hierarchyView.getRoot().getChildren().clear();
            tabPane.getTabs().clear();

            for (SimulationObject object : loadedObjects) {
                object.postDeserialize();
                final String id = object.name + " (" + object.uuid + ")";
                hierarchyView.getRoot().getChildren().add(new TreeItem<>(id));
                SimulationManager.getInstance().dataMap.put(id, object);
                hierarchyView.setCellFactory(tv -> new MyTreeCell(SimulationManager.getInstance().dataMap, tabPane));
            }

            SimulationManager.getInstance().simulationObjectList.clear();
            SimulationManager.getInstance().simulationObjectList.addAll(loadedObjects);

        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);
            stage.toFront();
            alert.setTitle("Error");
            alert.setHeaderText("Something went wrong, please try again");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
    }
    private void handleMenuItems() {
        checkMenuItemCanvasView.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            borderPane.getLeft().setManaged(!newValue);
            borderPane.getLeft().setVisible(!newValue);
            borderPane.getRight().setManaged(!newValue);
            borderPane.getRight().setVisible(!newValue);
            System.out.println(SimulationManager.getInstance().canvas.localToScene(0, 0).getX());
        });

        menuItemClear.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);
            stage.toFront();
            alert.setTitle("Clear All?");
            alert.setHeaderText("Are you sure you want to delete every object in the scene?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                Body body = SimulationManager.getInstance().world.getBodyList();
                while (body != null) {
                    Body next = body.getNext();
                    SimulationManager.getInstance().world.destroyBody(body);
                    body = next;
                }
                SimulationManager.getInstance().simulationObjectList.clear();
                SimulationManager.getInstance().dataMap.clear();
                hierarchyView.getRoot().getChildren().clear();
                tabPane.getTabs().clear();
            }
        });

        menuItemPositionCamReset.setOnAction(event -> {
            SimulationManager.getInstance().camX = 0;
            SimulationManager.getInstance().camY = 0;
        });

        menuItemScale.setOnAction(event -> {
            SimulationManager.getInstance().scaleX = 1;
            SimulationManager.getInstance().scaleY = 1;
        });

        saveMenuItem.setOnAction(event -> saveFile());

        loadMenuItem.setOnAction(event -> loadFile());
    }
    public void initialize() {
        SimulationManager.getInstance().canvas = canvas;
        SimulationManager.getInstance().gc = canvas.getGraphicsContext2D();
        presetScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        presetScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        presetFlowPane.setHgap(20);
        presetFlowPane.setVgap(20);

        createPresetButton.setOnAction(event -> createPresetWindow());
        menuItemPolygonVisualizer.setOnAction(event -> createPolygonVisualizerWindow());

        menuItemClose.setOnAction(event -> displayClosingAlertBox());

        menuItemHomeScreen.setOnAction(event -> returnToMainMenu());

        TreeItem<String> rootItem = new TreeItem<>("SampleScene");
        rootItem.setExpanded(true);
        hierarchyView.setRoot(rootItem);

        SimulationManager.getInstance().simulate();
        pauseButton.setOnAction(event -> {
            SimulationManager.getInstance().simulationPaused.setValue(!SimulationManager.getInstance().simulationPaused.getValue());
        });
//        SimulationManager.getInstance().simulationPaused.bind(mainWindow.focusedProperty().not());
        handleMenuItems();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::handleMousePressEvent);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleMouseDragEvent);
        canvas.addEventHandler(ScrollEvent.SCROLL, this::handleScrollEvent);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, this::handleMouseReleaseEvent);

        translation();
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
        CreatePresetController createPresetController = new CreatePresetController(presetWindow, presetHBox, presetList, presetFlowPane, hierarchyView,
                SimulationManager.getInstance().gc, objectsList, tabPane, scene);
        loader.setController(createPresetController);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root, 500, 980);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/stylesheetPresetWindow.css")).toExternalForm());
        presetWindow.setScene(scene);
        presetWindow.initModality(Modality.WINDOW_MODAL);
        presetWindow.initOwner(mainWindow);
        presetWindow.show();
    }

    public void createPolygonVisualizerWindow() {
        // TODO: Repetitive code in createPresetWindow, move save code into a method
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/polygonVisualizerPage.fxml"));
        Stage window = new Stage();
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        scene.getStylesheets().add(String.valueOf(getClass().getResource("/css/polygonVisualizer.css")));
        window.setTitle("Polygon visualizer");
        window.setScene(scene);
        window.initModality(Modality.WINDOW_MODAL);
        window.initOwner(mainWindow);
        window.show();
    }

    private void returnToMainMenu() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/stylesheets.css")).toExternalForm());
        ScreenController.getInstance().activate("mainMenu", SettingsSingleton.getInstance().language);
    }

	private void handleScrollEvent(ScrollEvent scrollevent) {
        final double increment = .1;
        double deltaY = scrollevent.getDeltaY();
        if (deltaY > 0) {
            SimulationManager.getInstance().scaleX += increment;
            SimulationManager.getInstance().scaleY += increment;
        }
        else if (deltaY < 0) {
            SimulationManager.getInstance().scaleX -= increment;
            SimulationManager.getInstance().scaleY -= increment;
        }
        SimulationManager.getInstance().scaleX = Math.max(0.1, Math.min(SimulationManager.getInstance().scaleX, 2.0));
        SimulationManager.getInstance().scaleY = Math.max(0.1, Math.min(SimulationManager.getInstance().scaleY, 2.0));
        scrollevent.consume();
	}
	//helper vars just for this method
	private Double last_x = 0.0; //wrapper classes for unassigned checks
	private Double last_y = 0.0;
    private boolean isDragging = true;

    private void handleMousePressEvent(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.MIDDLE) {
            last_x = mouseEvent.getX();
            last_y = mouseEvent.getY();
            isDragging = true;
        }
    }
	private void handleMouseDragEvent(MouseEvent mouseevent) {
		if (isDragging) {
            double deltaX = mouseevent.getX() - last_x;
            double deltaY = mouseevent.getY() - last_y;
            SimulationManager.getInstance().camX += deltaX;
            SimulationManager.getInstance().camY += deltaY;
            last_x = mouseevent.getX();
            last_y = mouseevent.getY();
        }
	}

    private void handleMouseReleaseEvent(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.MIDDLE) {
            isDragging = false;
        }
    }

    public void translation(){
        ArrayList<Label> labelArrayList = new ArrayList<>();
        ArrayList<Menu> menuArrayList = new ArrayList<>();
        ArrayList<MenuItem> menuItemArrayList = new ArrayList<>();
        ArrayList<CheckMenuItem> checkMenuItemArrayList = new ArrayList<>();
        labelArrayList.add(labelPresets);
        menuArrayList.add(menuFile);
        menuArrayList.add(menuEdit);
        menuArrayList.add(menuView);
        menuArrayList.add(menuWindow);
        menuArrayList.add(menuHelp);
        menuArrayList.add(menuCameraReset);
        menuItemArrayList.add(menuItemNew);
        menuItemArrayList.add(menuItemOpen);
        menuItemArrayList.add(menuItemClose);
        menuItemArrayList.add(menuItemClear);
        menuItemArrayList.add(menuItemSettings);
        menuItemArrayList.add(menuItemScale);
        menuItemArrayList.add(menuItemPolygonVisualizer);
        menuItemArrayList.add(menuItemFullScreen);
        menuItemArrayList.add(menuItemHomeScreen);
        menuItemArrayList.add(menuItemAbout);
        checkMenuItemArrayList.add(checkMenuItemCanvasView);

        Translation translation = new Translation(SettingsSingleton.getInstance().language);
        translation.translate(labelArrayList, menuArrayList, menuItemArrayList, checkMenuItemArrayList);
    }

}


