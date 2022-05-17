package math.irgups.smo_project;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Окно с вычислениями для СМО с неограниченным временем ожидания
 */
public class SMOUnLimitTimeCalculator {
    private final SMOUnLimitTime smo;
    private SMOUnLimitTimeCalculatorController controller;

    public SMOUnLimitTimeCalculator(Stage window, SMOUnLimitTime smo) {
        this.smo = smo;
        Stage stage = new Stage();
        Parent root;
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("unlimitTime-view.fxml"));
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
