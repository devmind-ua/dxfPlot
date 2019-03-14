package ua.in.devmind.dxfPlot;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Controller {
    @FXML
    private TextField rowCoordinateTextField;
    @FXML
    private TextField pointCoordinateTextField;
    @FXML
    private ListView<String> pointsListView;

    @FXML
    protected void enterRowTextFieldAction(KeyEvent event) {
        if (KeyCode.ENTER.equals(event.getCode())) {
            pointCoordinateTextField.requestFocus();
        }
    }

    @FXML
    protected void enterPointTextFieldAction(KeyEvent event) {
        if (KeyCode.ENTER.equals(event.getCode())) {
            pointsListView.getItems().add(rowCoordinateTextField.getText() + "," + pointCoordinateTextField.getText());
            pointCoordinateTextField.setText("");
            pointCoordinateTextField.requestFocus();
        }
    }
}
