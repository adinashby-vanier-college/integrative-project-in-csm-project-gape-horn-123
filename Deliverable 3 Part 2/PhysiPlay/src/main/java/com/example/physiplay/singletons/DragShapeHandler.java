package com.example.physiplay.singletons;

import com.example.physiplay.SimulationObject;
import com.example.physiplay.controllers.TabController;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;

public class DragShapeHandler implements EventHandler<MouseEvent> {

    public Node node ;
    GraphicsContext gc;
    TabPane tabPane;
    TreeView<String> hierarchyView;
    public TextField presetNameField;
    public TextField positionXField;
    public TextField positionYField;
    public TextField rotationField;
    public TextField scaleXField;
    public TextField scaleYField;
    SimulationObject simulationObject;

    public DragShapeHandler(Node node, GraphicsContext gc, TabPane tabPane, TreeView<String> hierarchyView, ArrayList<TextField> listTextFields, SimulationObject simulationObject) {
        this.node = node;
        this.gc = gc;
        this.tabPane = tabPane;
        this.hierarchyView = hierarchyView;
        this.presetNameField = listTextFields.getFirst();
        this.positionXField = listTextFields.get(1);
        this.positionYField = listTextFields.get(2);
        this.rotationField = listTextFields.get(3);
        this.scaleXField = listTextFields.get(4);
        this.scaleYField = listTextFields.get(5);
        this.simulationObject = simulationObject;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {

        }
        else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            SimulationManager.getInstance().simulationObjectList.add(this.simulationObject);
            gc.fillRect(event.getSceneX() - 360, event.getSceneY() - 35, 10, 10);
            Tab tab = new Tab(presetNameField.getText());
            tabPane.getTabs().add(tab);
            try {
                tab.setContent(makeTabContent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setTabPane(TabPane tabPane){
        this.tabPane = tabPane;
    }

    public ScrollPane makeTabContent() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gameObjectTab.fxml"));
        loader.setController(new TabController(getTextFields()));
        ScrollPane root = loader.load();
        root.getStylesheets().add(String.valueOf(getClass().getResource("/fonts/tabStylesheet.css")));
        hierarchyView.getRoot().getChildren().add(new TreeItem<>(simulationObject.name));
        /*VBox vBox = new VBox();
        HBox hBox = new HBox();
        Rectangle rectangle = new Rectangle(10,10);
        Label label = new Label("GameObject 1");
        hBox.getChildren().addAll(rectangle, label);
        Accordion accordion = new Accordion();
        TitledPane transformPane = new TitledPane();
        AnchorPane anchorPane = new AnchorPane();

        TitledPane rigidBody = new TitledPane();
        TitledPane audio = new TitledPane();
        accordion.getPanes().addAll(transformPane, rigidBody, audio);*/

        return root;
    }

    public ArrayList<TextField> getTextFields(){
        ArrayList<TextField> arrayList = new ArrayList<>();
        arrayList.add(presetNameField);
        arrayList.add(positionXField);
        arrayList.add(positionYField);
        arrayList.add(rotationField);
        arrayList.add(scaleXField);
        arrayList.add(scaleYField);
        return arrayList;
    }
}
