package math.irgups.smo_project;

import javafx.beans.property.SimpleStringProperty;

/**
 * Элемент в таблице
 */
public class DataTableSMO {
        public SimpleStringProperty name;
        public SimpleStringProperty  value;
            DataTableSMO(String name, String value) {
            this.name = new SimpleStringProperty(name);
            this.value = new SimpleStringProperty(value);
        }

        public String getValue() {
            return value.get();
        }

        public String getName() {
            return name.get();
        }
}