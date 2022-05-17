package math.irgups.smo_project;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Окно с решением для СМО с ограниченной очередью
 */
public class SMOLimitQueueCalculator {
    private final SMOLimitQueue smo;
    private SMOLimitQueueCalculatorController controller;
    public SMOLimitQueueCalculator(Stage window, SMOLimitQueue smo) {
        this.smo = smo;
        Stage stage = new Stage();
        Parent root;
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("limitQueue-view.fxml"));
        try {
            root = loader.load();
            controller = loader.getController();
            controller.setSmo(smo);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        controller.initData();
        stage.setOnCloseRequest(windowEvent -> window.show());
    }
}
