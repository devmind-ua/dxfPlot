package ua.in.devmind.dxfPlot.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.stage.FileChooser;
import ua.in.devmind.dxfPlot.generation.DxfGenerator;
import ua.in.devmind.dxfPlot.model.DataModel;

import java.io.File;

public class MenuController {

    private DataModel model;

    @FXML
    private MenuBar menuBar;

    public void initModel(DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;
    }

    @FXML
    protected void save() {
        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.setTitle("Save DXF file");
        saveFileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("AutoCAD Drawing eXchange Format", "*.dxf"));
        File file = saveFileChooser.showSaveDialog(menuBar.getScene().getWindow());
        if (file != null) {
            DxfGenerator.generateDxf(file, model.getPointsList());
        }
    }

    @FXML
    protected void exit() {
        model.savePointsToTempFile(true);
        Platform.exit();
    }
}
