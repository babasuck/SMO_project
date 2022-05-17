package math.irgups.smo_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Главный управляющий класс
 */
public class Main extends Application {
    private MainController controller;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        controller = fxmlLoader.getController();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setTitle("SMO modulator");
        stage.setScene(scene);
        stage.show();
    }

    public MainController getController() {
        return controller;
    }

    public static void main(String[] args) {
        launch();
    }
}