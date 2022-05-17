package math.irgups.smo_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Управляющий класс для СМО с ограниченной очередью
 */
public class SMOLimitQueueCalculatorController {
    @FXML
    private Canvas graphCanvas;
    @FXML
    private Canvas predelCanvas;
    @FXML
    private TableView<DataTableSMO> propTable;
    @FXML
    private TableView<DataTableSMO> startTableView;
    private SMOLimitQueue smo;

    public void initData() {
        ObservableList<DataTableSMO> startTable = FXCollections.observableArrayList(
                new DataTableSMO("Число каналов", String.format("%d", smo.getK())),
                new DataTableSMO("Интенсивность входящего потока(λ)", String.format("%.3f", smo.getIn())),
                new DataTableSMO("Интенсивность выходящего потока(μ)", String.format("%.3f", smo.getOut())),
                new DataTableSMO("Коэффициент загрузки(ρ)", String.format("%.3f", smo.getRo())),
                new DataTableSMO("Коэффициент использования(χ)", String.format("%.3f", smo.getXi())),
                new DataTableSMO("Средний интервал между заявками", String.format("%.3f", 1 / smo.getIn())),
                new DataTableSMO("Среднее время обслуживания", String.format("%.3f", 1 / smo.getOut()))
        );
        startTableView.setItems(startTable);
        TableColumn<DataTableSMO, String> nameProp = new TableColumn<>("Характеристика");
        nameProp.setCellValueFactory(new PropertyValueFactory<DataTableSMO, String>("name"));
        nameProp.setMinWidth(215);
        TableColumn<DataTableSMO, String> propValue = new TableColumn<>("Значение");
        propValue.setCellValueFactory(new PropertyValueFactory<DataTableSMO, String>("value"));
        startTableView.getColumns().add(nameProp);
        startTableView.getColumns().add(propValue);
        ObservableList<DataTableSMO> tableItems = FXCollections.observableArrayList(
                new DataTableSMO("Коэффициент простоя заявки", String.format("%.3f",smo.calculateKLazy())),
                new DataTableSMO("Вероятность отказа", String.format("%.3f", smo.calculateNoProbability())),
                new DataTableSMO("Среднее число занятых каналов", String.format("%.3f", smo.calculateBusyChannelsAvg())),
                new DataTableSMO("Среднее число заявок в очереди", String.format("%.3f", smo.calculateLineLengthAvg())),
                new DataTableSMO("Абсолютная пропускная способность", String.format("%.3f", smo.calculateAbs())),
                new DataTableSMO("Относительная пропускная способность", String.format("%.3f", smo.calculateRel())),
                new DataTableSMO("Среднее число ожидающих заявок", String.format("%.3f", smo.calculateNumSystemAvg())),
                new DataTableSMO("Среднее время ожидания", String.format("%.3f", smo.calculateWaitTimeLineAvg())),
                new DataTableSMO("Среднее время обсл.", String.format("%.3f", smo.calculateTimeAvg())),
                new DataTableSMO("Среднее время пребывания заявки в системе", String.format("%.3f", smo.calculateTimeSystemAvg()))
        );
        propTable.setItems(tableItems);
        nameProp = new TableColumn<>("Характеристика");
        nameProp.setCellValueFactory(new PropertyValueFactory<DataTableSMO, String>("name"));
        nameProp.setMinWidth(215);
        propValue = new TableColumn<>("Значение");
        propValue.setCellValueFactory(new PropertyValueFactory<DataTableSMO, String>("value"));
        propTable.getColumns().add(nameProp);
        propTable.getColumns().add(propValue);
        drawGraph();
        drawPredel();
    }

    /**
     * Отрисовка графа состояний
     */
    public void drawGraph( ) {
        GraphicsContext gc = graphCanvas.getGraphicsContext2D();
        gc.drawImage(new Image("/limitQueue.png"), 0,0);
    }

    public void drawPredel() {
        GraphicsContext gc = predelCanvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setFont(new Font(14));
        gc.strokeRect(0,0, 60, 50);
        gc.fillText("Ki",10, 25);
        gc.strokeRect(0,50, 60, 50);
        gc.fillText("Pi",10, 75);
        int i = 0;
        for(; i <= smo.getK() + 3; i++) {
            gc.strokeRect((i+1) * 60.0, 0, 60, 50);
            gc.fillText(String.valueOf(i), (i + 1) * 60.0 + 8,  25);
            gc.strokeRect((i+1) * 60.0, 50, 60, 50);
            gc.fillText(String.format("%.5f", smo.calculateProbability(i)), ( i + 1) * 60.0 + 8,  75);
        }
        gc.strokeRect((i+1) * 60.0, 0, 30, 50);
        gc.fillText("...", (i + 1) * 60.0 + 8,  25);
        gc.strokeRect((i+1) * 60.0, 50, 30, 50);
        gc.fillText("...", ( i + 1) * 60.0 + 8,  75);
    }

    public void setSmo(SMOLimitQueue smo) {
        this.smo = smo;
    }
}
