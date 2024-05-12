module mpp.training.project.cars.clientFX.main {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens cars to javafx.fxml;
    exports cars.gui to javafx.fxml;
}