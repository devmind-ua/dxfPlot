package ua.in.devmind.dxfPlot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import ua.in.devmind.dxfPlot.controller.MainViewController;
import ua.in.devmind.dxfPlot.data.Point;

public class DxfPlot extends Application {

    private final TextField primaryCoordinateTextField = new TextField();
    private final TextField secondaryCoordinateTextField = new TextField();
    private final ListView<Point> pointsListView = new ListView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/mainView.fxml"));
        Parent root = fxmlLoader.load();
        MainViewController mainViewController = fxmlLoader.getController();
        Scene scene = new Scene(root);
        KeyCombination undoKeyCombo = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
        Runnable undoPoint = () -> mainViewController.undoLatestPoint();
        scene.getAccelerators().put(undoKeyCombo, undoPoint);
        primaryStage.setTitle("dxfPlot");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
