package ua.in.devmind.dxfPlot.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ua.in.devmind.dxfPlot.data.Point;
import ua.in.devmind.dxfPlot.model.DataModel;

import java.awt.*;
import java.math.BigDecimal;

public class MainViewController {

    private DataModel model;

    @FXML
    private TextField primaryCoordinateTextField;
    @FXML
    private TextField secondaryCoordinateTextField;
    @FXML
    private ListView<Point> pointsListView;
    @FXML
    private Button deletePointsButton;

    private static boolean isValidBigDecimalString(String s) {
        try {
            new BigDecimal(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static void playSound() {
        ((Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.default")).run();
    }

    public void initModel(DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;
        initView();
    }

    public void initView() {
        pointsListView.setItems(model.getPointsList());
        pointsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        pointsListView.getSelectionModel().selectedItemProperty().addListener((observableValue, point, t1) -> {
            if (pointsListView.getSelectionModel().getSelectedItems().isEmpty()) {
                deletePointsButton.setDisable(true);
            } else {
                deletePointsButton.setDisable(false);
            }
        });
    }

    @FXML
    protected void onPrimaryCoordinateTextFieldKeyPressed(KeyEvent keyEvent) {
        if (KeyCode.ENTER.equals(keyEvent.getCode())) {
            if (keyEvent.isControlDown()) {
                model.savePointsToTempFile(true);
                primaryCoordinateTextField.setText("");
                secondaryCoordinateTextField.setText("");
                primaryCoordinateTextField.requestFocus();
            } else {
                if (!isValidBigDecimalString(primaryCoordinateTextField.getText())) {
                    playSound();
                    primaryCoordinateTextField.setText("");
                    primaryCoordinateTextField.requestFocus();
                    return;
                }
                secondaryCoordinateTextField.requestFocus();
            }
        } else if (KeyCode.Z.equals(keyEvent.getCode()) && keyEvent.isControlDown()) {
            Event.fireEvent(primaryCoordinateTextField.getScene(), keyEvent);
        }
    }

    @FXML
    protected void onSecondaryCoordinateTextFieldKeyPressed(KeyEvent keyEvent) {
        if (KeyCode.ENTER.equals(keyEvent.getCode())) {
            if (secondaryCoordinateTextField.getText().isEmpty() && keyEvent.isControlDown()) {
                model.savePointsToTempFile(true);
                primaryCoordinateTextField.setText("");
                secondaryCoordinateTextField.setText("");
                primaryCoordinateTextField.requestFocus();
                return;
            }
            if (!isValidBigDecimalString(secondaryCoordinateTextField.getText())) {
                playSound();
                secondaryCoordinateTextField.setText("");
                secondaryCoordinateTextField.requestFocus();
                return;
            }
            Point point = new Point(new BigDecimal(primaryCoordinateTextField.getText()), new BigDecimal(secondaryCoordinateTextField.getText()));
            if (pointsListView.getItems().contains(point)) {
                playSound();
                return;
            } else {
                model.addPoint(point);
            }
            if (keyEvent.isControlDown()) {
                model.savePointsToTempFile(true);
                primaryCoordinateTextField.setText("");
                secondaryCoordinateTextField.setText("");
                primaryCoordinateTextField.requestFocus();
            } else {
                secondaryCoordinateTextField.setText("");
                secondaryCoordinateTextField.requestFocus();
            }
        } else if (KeyCode.Z.equals(keyEvent.getCode()) && keyEvent.isControlDown()) {
            Event.fireEvent(secondaryCoordinateTextField.getScene(), keyEvent);
        }
    }

    @FXML
    protected void onDeletePointsButtonAction(ActionEvent event) {
        deleteSelectedPoints();
    }

    @FXML
    protected void onPointsListViewKeyPressed(KeyEvent keyEvent) {
        if (KeyCode.DELETE.equals(keyEvent.getCode())) {
            deleteSelectedPoints();
        }
    }

    private void deleteSelectedPoints() {
        pointsListView.getItems().removeAll(pointsListView.getSelectionModel().getSelectedItems());
        pointsListView.getSelectionModel().clearSelection();
        model.savePointsToTempFile(false);
    }

    public void undoLatestPoint() {
        if (!pointsListView.getItems().isEmpty()) {
            pointsListView.getItems().remove(0);
        }
    }
}
