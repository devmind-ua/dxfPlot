package ua.in.devmind.dxfPlot;

import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ua.in.devmind.dxfPlot.data.Point;

import java.awt.*;
import java.math.BigDecimal;

public class Main extends Application {

    private final TextField xTextField = new TextField();
    private final TextField yTextField = new TextField();
    private final ListView<Point> pointsListView = new ListView<>();

    public static void main(String[] args) {
        launch(args);
    }

    private static boolean isValidBigDecimalString(String s) {
        try {
            new BigDecimal(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void deleteSelectedPoints() {
        pointsListView.getItems().removeAll(pointsListView.getSelectionModel().getSelectedItems());
        pointsListView.getSelectionModel().clearSelection();
    }

    private static void playSound() {
        ((Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.default")).run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(5);
        root.setVgap(5);
        root.setPadding(new Insets(5));

        Label xLabel = new Label("x Coordinate:");
        Label yLabel = new Label("y Coordinate:");
        Label pointsListLabel = new Label("Points:");

        root.add(xLabel, 0, 0);
        root.add(xTextField, 1, 0);

        root.add(yLabel, 0, 1);
        root.add(yTextField, 1, 1);

        root.add(pointsListLabel, 0, 2);
        root.add(pointsListView, 0, 3, 2, 1);
        pointsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Scene scene = new Scene(root);

        Button deletePointsButton = new Button("Delete selected");
        deletePointsButton.setDisable(true);
        HBox buttonBox = new HBox(deletePointsButton);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        root.add(buttonBox, 1, 4);

        deletePointsButton.setOnAction(actionEvent -> deleteSelectedPoints());

        pointsListView.setOnKeyPressed(keyEvent -> {
            if (KeyCode.DELETE.equals(keyEvent.getCode())) {
                deleteSelectedPoints();
            }
        });

        pointsListView.getSelectionModel().selectedItemProperty().addListener((observableValue, point, t1) -> {
            if (pointsListView.getSelectionModel().getSelectedItems().isEmpty()) {
                deletePointsButton.setDisable(true);
            } else {
                deletePointsButton.setDisable(false);
            }
        });

        xTextField.setOnKeyPressed(keyEvent -> {
            if (KeyCode.ENTER.equals(keyEvent.getCode())) {
                if (keyEvent.isControlDown()) {
                    xTextField.setText("");
                    yTextField.setText("");
                    xTextField.requestFocus();
                } else {
                    if (!isValidBigDecimalString(xTextField.getText())) {
                        playSound();
                        xTextField.setText("");
                        xTextField.requestFocus();
                        return;
                    }
                    yTextField.requestFocus();
                }
            } else if (KeyCode.Z.equals(keyEvent.getCode()) && keyEvent.isControlDown()) {
                Event.fireEvent(scene, keyEvent);
            }
        });

        yTextField.setOnKeyPressed(keyEvent -> {
            if (KeyCode.ENTER.equals(keyEvent.getCode())) {
                if (yTextField.getText().isEmpty() && keyEvent.isControlDown()) {
                    xTextField.setText("");
                    yTextField.setText("");
                    xTextField.requestFocus();
                    return;
                }
                if (!isValidBigDecimalString(yTextField.getText())) {
                    playSound();
                    yTextField.setText("");
                    yTextField.requestFocus();
                    return;
                }
                Point point = new Point(new BigDecimal(xTextField.getText()), new BigDecimal(yTextField.getText()));
                if (pointsListView.getItems().contains(point)) {
                    playSound();
                    return;
                } else {
                    pointsListView.getItems().add(0, point);
                }
                if (keyEvent.isControlDown()) {
                    xTextField.setText("");
                    yTextField.setText("");
                    xTextField.requestFocus();
                } else {
                    yTextField.setText("");
                    yTextField.requestFocus();
                }
            } else if (KeyCode.Z.equals(keyEvent.getCode()) && keyEvent.isControlDown()) {
                Event.fireEvent(scene, keyEvent);
            }
        });

        KeyCombination undoKeyCombo = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
        Runnable undoPoint = () -> {
            if (!pointsListView.getItems().isEmpty()) {
                pointsListView.getItems().remove(0);
            }
        };
        scene.getAccelerators().put(undoKeyCombo, undoPoint);

        primaryStage.setTitle("dxfPlot");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
