package ua.in.devmind.dxfPlot.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ua.in.devmind.dxfPlot.event.CoordinatesSwappedEvent;
import ua.in.devmind.dxfPlot.model.DataModel;
import ua.in.devmind.dxfPlot.model.data.Point;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Optional;

public class MainViewController implements EventHandler<CoordinatesSwappedEvent> {

    private DataModel model;

    @FXML
    private TextField primaryCoordinateTextField;
    @FXML
    private Label primaryCoordinateLabel;
    @FXML
    private TextField secondaryCoordinateTextField;
    @FXML
    private Label secondaryCoordinateLabel;
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
        this.model.addCoordinatesSwappedHandler(this);
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
            if (!model.createAndAddPoint(new BigDecimal(primaryCoordinateTextField.getText()), new BigDecimal(secondaryCoordinateTextField.getText()))) {
                playSound();
                return;
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
        model.getPointsList().removeAll(pointsListView.getSelectionModel().getSelectedItems());
        pointsListView.getSelectionModel().clearSelection();
        model.savePointsToTempFile(false);
    }

    public void undoLatestPoint() {
        if (!pointsListView.getItems().isEmpty()) {
            pointsListView.getItems().remove(0);
        }
    }

    @Override
    public void handle(CoordinatesSwappedEvent coordinatesSwappedEvent) {
        if (!model.getPointsList().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Swap coordinates");
            alert.setHeaderText("Points list is not empty");
            alert.setContentText("Do you want to swap coordinates for existing points?");
            ButtonType yesBtn = new ButtonType("Yes");
            ButtonType noBtn = new ButtonType("No");
            alert.getButtonTypes().setAll(yesBtn, noBtn);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.orElse(noBtn) == yesBtn) {
                model.swapCoordinatesForExistingPoints();
            }
        }
        if (coordinatesSwappedEvent.isCoordinatesSwapped()) {
            primaryCoordinateLabel.setText("Y:");
            secondaryCoordinateLabel.setText("X:");
        } else {
            primaryCoordinateLabel.setText("X:");
            secondaryCoordinateLabel.setText("Y:");
        }
    }
}
