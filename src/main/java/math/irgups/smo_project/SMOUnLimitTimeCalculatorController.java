package math.irgups.smo_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Управляющий класс окна для СМО с неограниченным временем ожидания
 */
public class SMOUnLimitTimeCalculatorController {
    @FXML
    private TableView<DataTableSMO> startTableView;
    @FXML
    private Canvas graphCanvas;
    @FXML
    private Canvas predelCanvas;
    @FXML
    private TableView<DataTableSMO> propTable;
    private SMOUnLimitTime smo;

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
                new DataTableSMO("Коэффициент простоя системы", String.format("%.3f",smo.calculateKLazy())),
                new DataTableSMO("Среднее число занятых каналов", String.format("%.3f", smo.calculateBusyChannelsAvg())),
                new DataTableSMO("Среднее число заявок в очереди", String.format("%.3f", smo.calculateLineLengthAvg())),
                new DataTableSMO("Абсолютная пропускная способность", String.format("%.3f", smo.getAbs())),
                new DataTableSMO("Относительная пропускная способность", String.format("%d", smo.getRel())),
                new DataTableSMO("Среднее число ожидающих заявок", String.format("%.3f", smo.calculateNumSystemAvg())),
                new DataTableSMO("Среднее время ожидания", String.format("%.3f", smo.calculateWaitTimeLineAvg())),
                new DataTableSMO("Среднее время пребывания заявки в системе", String.format("%.3f", smo.calculateTimeSystemAvg())),
                new DataTableSMO("Вероятность, что ожидает хотя бы одна заявка", String.format("%.3f", smo.calculateOneInLine())),
                new DataTableSMO("Вероятность, что завяка ожидает обслуживания", String.format("%.3f", smo.calculateWaitingProbability()))
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

    public void setSmo(SMOUnLimitTime smo) {
        this.smo = smo;
    }

    /**
     * Отрисовка графа состояний
     */
    public void drawGraph( ) {
        GraphicsContext gc = graphCanvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setFont(new Font(14));
        int i = 0;
        for(; i < smo.getK() + 1; i++) {
            // Состояние
            gc.strokeRect(i * 100.0, 0, 50, 50);
            // Текст в состоянии
            gc.fillText("S" + i, i * 100.0 + 12.5, 25);
            // Линия верхняя
            gc.strokeLine(i * 100.0 + 50.0, 12.5, (i+1) * 100, 12.5);
            // Интенсивность входа λ
            gc.fillText("λ",(i+1) * 100 - 25, 10.5);
            // Стрелка верхняя
            gc.strokeLine((i+1) * 100, 12.5, (i+1) * 100 - 10, 12.5 - 7);
            gc.strokeLine((i+1) * 100, 12.5, (i+1) * 100 - 10, 12.5 + 7);
            // Линия нижняя
            gc.strokeLine(i * 100.0 + 50.0, 37.5, (i+1) * 100, 37.5);
            // Интенсивность обслуживания µ
            if(i + 1 == smo.getK() + 1) {
                gc.fillText(String.format("%dµ", i + 1),(i+1) * 100 - 35, 47.5);
            }
            else
                gc.fillText(String.format("%dµ", i + 1), (i+1) * 100 - 35, 47.5);
            // Стрелка нижняя
            gc.strokeLine(i * 100.0 + 50.0, 37.5, i * 100.0 + 50.0 + 10, 37.5 + 7);
            gc.strokeLine(i * 100.0 + 50.0, 37.5, i * 100.0 + 50.0 + 10, 37.5 - 7);
        }
        // Точки верхние
        gc.strokeLine(i * 100 + 3, 12.5, i * 100 + 6, 12.5);
        gc.strokeLine(i * 100 + 9, 12.5, i * 100 + 12, 12.5);
        gc.strokeLine(i * 100 + 15, 12.5, i * 100 + 18, 12.5);
        // Точки нижние
        gc.strokeLine(i * 100 + 3, 37.5, i * 100 + 6, 37.5);
        gc.strokeLine(i * 100 + 9, 37.5, i * 100 + 12, 37.5);
        gc.strokeLine(i * 100 + 15, 37.5, i * 100 + 18, 37.5);
        // Стрелка верхняя
        gc.strokeLine(i * 100.0 + 21, 12.5, i * 100 + 50, 12.5);
        // Лямда
        gc.fillText("λ",i * 100.0 + 21, 10.5);
        gc.strokeLine(i * 100 + 50, 12.5, i * 100 + 50 - 10, 12.5 - 7);
        gc.strokeLine(i * 100 + 50, 12.5, i * 100 + 50 - 10, 12.5 + 7);
        // Стрелка нижняя
        gc.strokeLine(i * 100.0 + 21, 37.5, i * 100 + 50, 37.5);
        gc.fillText("(k+j)µ", i * 100.0 + 10, 53.5);
        gc.strokeLine(i * 100.0 + 21, 37.5, i * 100.0 + 21 + 10, 37.5 + 7);
        gc.strokeLine(i * 100.0 + 21, 37.5, i * 100.0 + 21 + 10, 37.5 - 7);
        // Общий вид с очередью
        gc.strokeRect(i * 100 + 50, 0, 50, 50);
        gc.fillText("S" + "k" + "+ j", i * 100 + 50 + 10, 25);

        // Стрелка верхняя
        gc.strokeLine(i * 100.0 + 100, 12.5, i * 100 + 150, 12.5);
        // Лямда
        gc.fillText("λ",i * 100.0 + 100 + 20, 10.5);
        gc.strokeLine(i * 100 + 150, 12.5, i * 100 + 150 - 10, 12.5 - 7);
        gc.strokeLine(i * 100 + 150, 12.5, i * 100 + 150 - 10, 12.5 + 7);
        // Стрелка нижняя
        gc.strokeLine(i * 100.0 + 100, 37.5, i * 100 + 150, 37.5);
        gc.strokeLine(i * 100.0 + 100, 37.5, i * 100.0 + 100 + 10, 37.5 + 7);
        gc.strokeLine(i * 100.0 + 100, 37.5, i * 100.0 + 100 + 10, 37.5 - 7);
        gc.fillText("(k+j+1)µ", i * 100.0 + 104, 53.5);
        // Точки верхние
        gc.strokeLine(i * 100 + 150 + 3, 12.5, i * 100 + 150 + 6, 12.5);
        gc.strokeLine(i * 100 + 150 + 9, 12.5, i * 100 + 150 + 12, 12.5);
        gc.strokeLine(i * 100 + 150 + 15, 12.5, i * 100 + 150 + 18, 12.5);
        // Точки нижние
        gc.strokeLine(i * 100 + 150 + 3, 37.5, i * 100 + 150 + 6, 37.5);
        gc.strokeLine(i * 100 + 150 + 9, 37.5, i * 100 + 150 + 12, 37.5);
        gc.strokeLine(i * 100 + 150 + 15, 37.5, i * 100 + 150 + 18, 37.5);
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
        for(; i <= smo.getK() + 1; i++) {
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



}
