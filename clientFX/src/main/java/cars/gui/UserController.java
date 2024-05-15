package cars.gui;

import cars.Car;
import cars.IObserver;
import cars.IService;
import cars.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @FXML
    private ComboBox<String> carBrandComboBox;

    @FXML
    private TextField txtBrand;
    @FXML
    private TextField txtHp;

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

        //carBrandComboBox
        List<String> brands = new ArrayList<>();
        for (Car car : cars)
            brands.add(car.getBrand());
        this.carBrandComboBox.setItems(FXCollections.observableArrayList(brands));
    }

    @Override
    public void carSaved(Car car) throws Exception {

    }

    @Override
    public void carDeleted(Long id) throws Exception {

    }

    public void onFiltreaza(ActionEvent actionEvent) throws Exception {
        String brand = this.carBrandComboBox.getValue();
        if (!Objects.equals(brand, ""))
            this.modelCar.setAll(this.service.getAllCarsBrand(brand));
    }

    public void onAnuleaza(ActionEvent actionEvent) throws Exception {
        var cars = this.service.getAllCars();
        modelCar.setAll(cars);
    }

    public void onAddCar(ActionEvent actionEvent) throws Exception {
        String brand = this.txtBrand.getText();
        int hp = Integer.parseInt(this.txtHp.getText());

        if (Objects.equals(brand, "") || hp < 1)
        {
            MessageWindow.showMessage(null, Alert.AlertType.ERROR, "Error", "Invalid input!");
        }
        else
        {
            this.service.saveCar(brand, hp);
        }
    }
}
