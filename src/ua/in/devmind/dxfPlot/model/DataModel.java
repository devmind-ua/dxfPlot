package ua.in.devmind.dxfPlot.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ua.in.devmind.dxfPlot.data.Point;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Collectors;

public class DataModel {

    private static final String TMP_FILE_PREFIX = "dxfPlot-";
    private static final String TMP_FILE_SUFFIX = ".tmp";
    private static final String POINTS_DELIMITER = ";";

    private final ObservableList<Point> pointsList = FXCollections.observableList(new ArrayList<>());

    private final ArrayList<Point> recentPoints = new ArrayList<>();

    private File tempFile;

    public static File getLatestTempFile() {
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        File[] tempFiles = tmpDir.listFiles((dir, name) -> name.startsWith(TMP_FILE_PREFIX) && name.endsWith(TMP_FILE_SUFFIX));
        return Arrays.stream(tempFiles)
                .sorted(Comparator.comparingLong(File::lastModified).reversed())
                .findFirst()
                .orElse(null);
    }

    public void init(File _tempFile) {
        if (_tempFile == null) {
            try {
                tempFile = File.createTempFile(TMP_FILE_PREFIX, TMP_FILE_SUFFIX);
            } catch (IOException e) {
                System.exit(2);
            }
        } else {
            tempFile = _tempFile;
            loadDataFromTempFile(tempFile);
        }
    }

    public void addPoint(Point point) {
        pointsList.add(0, point);
        recentPoints.add(point);
    }

    public void savePointsToTempFile(boolean recentOnly) {
        if ((recentOnly && recentPoints.isEmpty()) || pointsList.isEmpty()) {
            return;
        }
        var oldTempFile = tempFile;
        if (!recentOnly) {
            try {
                tempFile = File.createTempFile(TMP_FILE_PREFIX, TMP_FILE_SUFFIX);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileWriter fw = new FileWriter(tempFile, recentOnly);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            if (recentOnly) {
                if (tempFile.length() > 0) {
                    out.print(POINTS_DELIMITER);
                }
                out.print(recentPoints.stream()
                        .map(Point::toRawString)
                        .collect(Collectors.joining(POINTS_DELIMITER)));
                recentPoints.clear();
            } else {
                out.print(pointsList.stream()
                        .map(Point::toRawString)
                        .collect(Collectors.joining(POINTS_DELIMITER)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!recentOnly) {
            oldTempFile.delete();
        }
    }

    private void loadDataFromTempFile(File tempFile) {
        try (Scanner fileScanner = new Scanner(tempFile)) {
            fileScanner.useDelimiter(";");
            while (fileScanner.hasNext()) {
                String pointString = fileScanner.next();
                String[] coordinates = pointString.split(Point.COORDINATES_DELIMITER);
                Point point = new Point(new BigDecimal(coordinates[0]), new BigDecimal(coordinates[1]));
                pointsList.add(point);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Point> getPointsList() {
        return pointsList;
    }
}
