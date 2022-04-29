module math.irgups.smo_project {
    requires javafx.controls;
    requires javafx.fxml;


    opens math.irgups.smo_project to javafx.fxml;
    exports math.irgups.smo_project;
}