package ua.in.devmind.dxfPlot;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ua.in.devmind.dxfPlot.data.Point;

import java.awt.*;
import java.math.BigDecimal;

public class Main extends Application {

    private final TextField xTextField = new TextField();
    private final TextField yTextField = new TextField();
    private final ListView<Point> pointsListView = new ListView<>();

    @Override
    public void start(Stage primaryStage) throws Exception{
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(5);
        root.setVgap(5);

        Label xLabel = new Label("x Coordinate:");
        Label yLabel = new Label("y Coordinate:");
        Label pointsListLabel = new Label("Points:");

        root.add(xLabel, 0, 0);
        root.add(xTextField, 1, 0);

        root.add(yLabel, 0, 1);
        root.add(yTextField, 1, 1);

        root.add(pointsListLabel, 0, 2);
        root.add(pointsListView, 0, 3, 2, 1);

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
            }
        });

        yTextField.setOnKeyPressed(keyEvent -> {
            if (KeyCode.ENTER.equals(keyEvent.getCode())) {
                if (!isValidBigDecimalString(yTextField.getText())) {
                    playSound();
                    yTextField.setText("");
                    yTextField.requestFocus();
                    return;
                }
                Point point = new Point(new BigDecimal(xTextField.getText()), new BigDecimal(yTextField.getText()));
                pointsListView.getItems().add(0, point);
                if (keyEvent.isControlDown()) {
                    xTextField.setText("");
                    yTextField.setText("");
                    xTextField.requestFocus();
                } else {
                    yTextField.setText("");
                    yTextField.requestFocus();
                }
            }
        });

        primaryStage.setTitle("dxfPlot");
        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.show();
    }


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

    private static void playSound() {
        ((Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.default")).run();
    }
}
