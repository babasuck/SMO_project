package math.irgups.smo_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Управляющий класс основного окна
 */
public class MainController {
    @FXML
    private Text lOchText;
    @FXML
    private TextField lOchField;
    @FXML
    private ChoiceBox<String> smoChoiseBox;
    @FXML
    private Text tOjText;
    @FXML
    private Text epsText;
    @FXML
    private TextField epsCount;
    @FXML
    private Button enter_button;
    @FXML
    private TextField channelsField;
    @FXML
    private TextField inField;
    @FXML
    private TextField outField;
    @FXML
    private TextField exitField;
    private SMO smo;
    private String smoType;

    public void initialize() {
        initData();
    }

    public void initData() {
        lOchField.setVisible(false);
        lOchText.setVisible(false);
        ObservableList<String> smo_items = FXCollections.observableArrayList
                ("С ограниченным временем ожидания", "С неограниченным временем ожидания",
                        "С ограниченной очередью");
        smoChoiseBox.setItems(smo_items);
        smoChoiseBox.setOnAction(actionEvent -> {
            smoType = smoChoiseBox.getValue();
            switch (smoType) {
                case "С ограниченным временем ожидания" -> {
                    exitField.setVisible(true);
                    tOjText.setVisible(true);
                    epsText.setVisible(true);
                    epsCount.setVisible(true);

                    lOchField.setVisible(false);
                    lOchText.setVisible(false);
                }
                case "С неограниченным временем ожидания" -> {
                    exitField.setVisible(false);
                    tOjText.setVisible(false);
                    epsText.setVisible(false);
                    epsCount.setVisible(false);

                    lOchField.setVisible(false);
                    lOchText.setVisible(false);
                }
                case "С ограниченной очередью" -> {
                    lOchText.setVisible(true);
                    lOchField.setVisible(true);

                    exitField.setVisible(false);
                    tOjText.setVisible(false);
                    epsText.setVisible(false);
                    epsCount.setVisible(false);
                }
            }
        });
        smoChoiseBox.setValue("С ограниченным временем ожидания");
    }

    public void enter_buttonAction() {
        switch (smoType) {
            case "С ограниченным временем ожидания" -> createSMOLimitTime();
            case "С неограниченным временем ожидания" -> createSMOUnLimitTime();
            case "С ограниченной очередью" -> createSMOLimitQueue();
        }
    }

    public void createSMOLimitTime() {
        if(channelsField.getText().isEmpty() || inField.getText().isEmpty() || outField.getText().isEmpty() ||
                exitField.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Некорректные данные.").showAndWait();
            return;
        }
        int eps = epsCount.getText().isEmpty() ? 6 : Integer.parseInt(epsCount.getText());
        int channels = Integer.parseInt(channelsField.getText());
        double in = Double.parseDouble(inField.getText());
        double out = Double.parseDouble(outField.getText());
        double v = Double.parseDouble(exitField.getText());
        smo = new SMOLimitTime(channels, in, out, v, eps);
        Stage window = (Stage) enter_button.getScene().getWindow();
        window.hide();
        new SMOLimitTimeCalculator(window, (SMOLimitTime) smo);
    }

    public void createSMOUnLimitTime() {
        if(channelsField.getText().isEmpty() || inField.getText().isEmpty() || outField.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Некорректные данные.").showAndWait();
            return;
        }
        int channels = Integer.parseInt(channelsField.getText());
        double in = Double.parseDouble(inField.getText());
        double out = Double.parseDouble(outField.getText());
        smo = new SMOUnLimitTime(channels, in, out);
        Stage window = (Stage) enter_button.getScene().getWindow();
        window.hide();
        new SMOUnLimitTimeCalculator(window, (SMOUnLimitTime)smo);
    }

    public void createSMOLimitQueue() {
        if(channelsField.getText().isEmpty() || inField.getText().isEmpty() || outField.getText().isEmpty()
        || lOchField.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Некорректные данные.").showAndWait();
            return;
        }
        int channels = Integer.parseInt(channelsField.getText());
        double in = Double.parseDouble(inField.getText());
        double out = Double.parseDouble(outField.getText());
        int queueLen = Integer.parseInt(lOchField.getText());
        smo = new SMOLimitQueue(channels, in, out, queueLen);
        Stage window = (Stage) enter_button.getScene().getWindow();
        window.hide();
        new SMOLimitQueueCalculator(window, (SMOLimitQueue) smo);
    }

    public SMO getSmo() {
        return smo;
    }
}