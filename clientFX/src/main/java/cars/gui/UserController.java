package cars.gui;

import cars.Car;
import cars.IObserver;
import cars.IService;
import cars.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Timestamp;

public class UserController implements IObserver {
    private IService service;
    private User user;

    @FXML
    private TableView<Car> carTableView;
    private ObservableList<Car> modelCar = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Car, String> carBrandColumn;
    @FXML
    private TableColumn<Car, Integer> carHpColumn;

    public void init_controller(IService service, User user) throws Exception
    {
        System.out.println("init user controller!");
        this.service = service;
        this.user = user;

        initModel();
    }

    @FXML
    public void initialize()
    {
        carBrandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        carHpColumn.setCellValueFactory(new PropertyValueFactory<>("hp"));
        carTableView.setItems(modelCar);
    }

    private void initModel() throws Exception
    {
        System.out.println("init model!!!!!!!!!!!!");
        var cars = this.service.getAllCars();
        modelCar.setAll(cars);
    }

    @Override
    public void carSaved(Car car) throws Exception {

    }

    @Override
    public void carDeleted(Long id) throws Exception {

    }
}
