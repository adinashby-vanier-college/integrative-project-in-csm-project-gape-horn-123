package com.example.physiplay.components;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class DragShapeHandler implements EventHandler<MouseEvent> {

    public Node node ;
    GraphicsContext gc;
    TabPane tabPane;
    TreeView<String> hierarchyView;

    public DragShapeHandler(Node node, GraphicsContext gc, TabPane tabPane, TreeView<String> hierarchyView) {
        this.node = node;
        this.gc = gc;
        this.tabPane = tabPane;
        this.hierarchyView = hierarchyView;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {

        }
        else if (event.getEventType() == MouseEvent.MOUSE_RELEASED){
            gc.fillRect(event.getSceneX() - 360, event.getSceneY() - 35, 10, 10);
            Tab tab = new Tab("GameObject 1");
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
        ScrollPane root = loader.load();
        root.getStylesheets().add(String.valueOf(getClass().getResource("/fonts/tabStylesheet.css")));
        hierarchyView.getRoot().getChildren().add(new TreeItem<>("GameObject1"));
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
}
