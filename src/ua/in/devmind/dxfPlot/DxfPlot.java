package ua.in.devmind.dxfPlot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ua.in.devmind.dxfPlot.controller.MainViewController;
import ua.in.devmind.dxfPlot.controller.MenuController;
import ua.in.devmind.dxfPlot.model.data.Point;
import ua.in.devmind.dxfPlot.model.DataModel;

public class DxfPlot extends Application {

    private final TextField primaryCoordinateTextField = new TextField();
    private final TextField secondaryCoordinateTextField = new TextField();
    private final ListView<Point> pointsListView = new ListView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane root = new BorderPane();

        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("view/mainView.fxml"));
        root.setCenter(mainViewLoader.load());
        MainViewController mainViewController = mainViewLoader.getController();

        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("view/menu.fxml"));
        root.setTop(menuLoader.load());
        MenuController menuController = menuLoader.getController();

        DataModel model = new DataModel();
        model.init(DataModel.getLatestTempFile());
        mainViewController.initModel(model);
        menuController.initModel(model);

        Scene scene = new Scene(root);
        KeyCombination undoKeyCombo = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
        Runnable undoPoint = () -> mainViewController.undoLatestPoint();
        scene.getAccelerators().put(undoKeyCombo, undoPoint);
        primaryStage.setTitle("dxfPlot");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
